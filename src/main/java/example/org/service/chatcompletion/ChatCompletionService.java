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

    /**
     * Constructor for the ChatCompletionService class.
     *
     * @param gameLogicService An instance of the GameLogicService for game logic operations.
     */
    public ChatCompletionService(GameLogicService gameLogicService) {
        String token = System.getenv("OPENAI_API_KEY");
        service = new OpenAiService(token, Duration.ofSeconds(30));
        this.gameLogicService = gameLogicService;
    }

    /**
     * Converts a list of messages into ChatMessage objects for interaction with the chat model.
     *
     * @param messages The list of messages to convert.
     * @return A list of ChatMessage objects.
     */
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

    /**
     * Generates a ChatMessage representation of the game status.
     *
     * @param gameStatus The current game status.
     * @return Formatted ChatMessage.
     */
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

    /**
     * Retrieves a Flowable of description for the provided game status.
     *
     * @param gameStatus The GameStatus instance for which description is needed.
     * @return A Flowable of description for the given game status.
     */
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

    /**
     * Advances the game status to the next state based on the provided game status.
     *
     * @param gameStatus The current GameStatus instance to progress from.
     * @return The next GameStatus instance after a game state transition.
     */
    public GameStatus getNewGameStatus(GameStatus gameStatus) {
        List<ChatMessage> messages = convertToChatMessages(gameStatus.getMessages());
        messages.add(messages.size() - 2, getFunctionMessage(gameStatus));

        FunctionExecutor functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_last_turn_info")
                .description("get the information about the last turn")
                .executor(GetNextTurn.class, x -> gameLogicService.updateGameStatus(gameStatus, x))
                .build()));

        ChatCompletionRequest completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("get_last_turn_info"))
                .n(1)
                .logitBias(new HashMap<>())
                .build();

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
