package example.org.service.chatcompletion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import example.org.dto.GameStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ChatCompletionService {
    private final OpenAiService service;
    private final String SYSTEM_MESSAGE = "Please perform the function of a text adventure game, following the rules listed below:" +
            "\nStay in character as a text adventure game and respond to commands the way a text adventure game should." +
            "\nYour responses should be very short, just one sentence. Respond with just on sentence." +
            "\nTime must change every round according to the user input" +
            "\nDay changes every few rounds as time passes" +
            "\nWeather changes every few rounds";

    public ChatCompletionService() {
        String token = System.getenv("OPENAI_API_KEY");
        service = new OpenAiService(token);
    }

    private GameStatus updateGameStatus(GameStatus gameStatus, GetNextTurn nextTurn) {
        GameStatus newGameStatus = new GameStatus();
        newGameStatus.setRound(gameStatus.getRound() + 1);
        newGameStatus.setTime(nextTurn.time);
        newGameStatus.setDay(nextTurn.day);
        newGameStatus.setWeather(nextTurn.weather);
        newGameStatus.setXp(nextTurn.xp);
        newGameStatus.setInventory(nextTurn.inventory);
        List<String> messages = gameStatus.getMessages();
        messages.add(nextTurn.description);
        newGameStatus.setMessages(messages);
        return newGameStatus;
    }

    private GetNextTurnRequest convertToRequest(GameStatus gameStatus) {
        GetNextTurnRequest request = new GetNextTurnRequest();
        request.time = gameStatus.getTime();
        request.day = gameStatus.getDay();
        request.weather = gameStatus.getWeather();
        request.xp = gameStatus.getXp();
        request.inventory = gameStatus.getInventory();
        return request;
    }

    private ChatMessage getFunctionMessage(GameStatus gameStatus) {
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        String arguments;
        try {
            arguments = mapper.writeValueAsString(convertToRequest(gameStatus));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ChatMessage(ChatMessageRole.FUNCTION.value(), arguments, "get_game_status");
    }

    private List<ChatMessage> convertToChatMessages(List<String> messages) {
        List<ChatMessage> chatMessages = new LinkedList<>();
        chatMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), SYSTEM_MESSAGE));
        boolean isAssistant = true;
        for (String message : messages) {
            chatMessages.add(new ChatMessage(isAssistant ? ChatMessageRole.ASSISTANT.value() : ChatMessageRole.USER.value(), message));
            isAssistant = !isAssistant;
        }
        return chatMessages;
    }

    public GameStatus getCompletion(GameStatus gameStatus) {
        List<ChatMessage> messages = convertToChatMessages(gameStatus.getMessages());
        messages.add(getFunctionMessage(gameStatus));

        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_next_turn")
                .description("get the next turn of the game include description")
                .executor(GetNextTurn.class, x -> updateGameStatus(gameStatus, x))
                .build()));

        ChatCompletionRequest completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("get_next_turn"))
                .n(1)
                .logitBias(new HashMap<>())
                .build();

        // for debugging
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            System.out.println(mapper.writeValueAsString(completionRequest));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ChatMessage responseMessage = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        if (responseMessage.getFunctionCall() == null) {
            throw new RuntimeException("No function call returned");
        }
        ChatFunctionCall ResponseFunctionCall = responseMessage.getFunctionCall();
        return functionExecutor.execute(ResponseFunctionCall);
    }
}
