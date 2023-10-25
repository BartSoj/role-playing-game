package example.org.service.chatcompletion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import example.org.dto.GameStatus;
import example.org.service.gamelogic.GameLogicService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class ChatCompletionService {
    private final OpenAiService service;
    private final GameLogicService gameLogicService;

    public ChatCompletionService(GameLogicService gameLogicService) {
        String token = System.getenv("OPENAI_API_KEY");
        service = new OpenAiService(token, Duration.ofSeconds(30));
        this.gameLogicService = gameLogicService;
    }

    private List<ChatMessage> convertToChatMessages(List<String> messages) {
        List<ChatMessage> chatMessages = new LinkedList<>();
        chatMessages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), gameLogicService.getSystemMessage()));
        boolean isAssistant = true;
        for (String message : messages) {
            chatMessages.add(new ChatMessage(isAssistant ? ChatMessageRole.ASSISTANT.value() : ChatMessageRole.USER.value(), message));
            isAssistant = !isAssistant;
        }
        return chatMessages;
    }

    private ChatMessage getFunctionMessage(GameStatus gameStatus) {
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        String arguments;
        try {
            GetNextTurnRequest request = gameLogicService.convertToRequest(gameStatus);
            arguments = mapper.readValue(mapper.writeValueAsString(request), JsonNode.class).toPrettyString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ChatMessage(ChatMessageRole.FUNCTION.value(), arguments, "get_game_status");
    }

    public Flowable<String> getFlowableDescription(GameStatus gameStatus) {
        List<ChatMessage> messages = convertToChatMessages(gameStatus.getMessages());
        messages.add(getFunctionMessage(gameStatus));

        ChatCompletionRequest descriptionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .logitBias(new HashMap<>())
                .build();

        Flowable<ChatCompletionChunk> flowable = service.streamChatCompletion(descriptionRequest);

        return service.mapStreamToAccumulator(flowable)
                .filter(accumulator -> accumulator.getMessageChunk().getContent() != null)
                .map(accumulator -> accumulator.getMessageChunk().getContent());
    }

    public GameStatus getNewGameStatus(GameStatus gameStatus) {
        List<ChatMessage> messages = convertToChatMessages(gameStatus.getMessages());
        messages.add(messages.size() - 2, getFunctionMessage(gameStatus));

        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_next_turn")
                .description("get the next turn of the game")
                .executor(GetNextTurn.class, x -> gameLogicService.updateGameStatus(gameStatus, x))
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
//        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
//        mapper = mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        try {
//            System.out.println(mapper.writeValueAsString(completionRequest));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        ChatMessage responseMessage = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        if (responseMessage.getFunctionCall() == null) {
            throw new RuntimeException("No function call returned");
        }
        ChatFunctionCall responseMessageFunctionCall = responseMessage.getFunctionCall();
        GameStatus newGameStatus = functionExecutor.execute(responseMessageFunctionCall);
        log.debug("GameStatus: {}", newGameStatus);
        return newGameStatus;
    }
}
