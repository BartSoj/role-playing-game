package example.org;

import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AiResponse {
    private static final Logger logger = LoggerFactory.getLogger(AiResponse.class);
    private final OpenAiService service;
    private final List<ChatMessage> messages;
    private final FunctionExecutor functionExecutor;
    private final List<String> itemsList;

    public AiResponse() {
        String token = System.getenv("OPENAI_API_KEY");
        service = new OpenAiService(token);
        messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);
        itemsList = new ArrayList<>();

        functionExecutor = new FunctionExecutor(List.of(ChatFunction.builder()
                .name("get_weapon")
                .description("Get a weapon")
                .executor(GetItem.GetWeapon.class, w -> {
                    itemsList.add(w.name);
                    return w;
                })
                .build()));
    }

    private ChatCompletionRequest buildCompletionRequest() {
        return ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();
    }

    public String getResponse(String message) {
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));
        ChatMessage responseMessage = service.createChatCompletion(buildCompletionRequest()).getChoices().get(0).getMessage();
        logger.debug(responseMessage.toString());
        messages.add(responseMessage);
        ChatFunctionCall functionCall = responseMessage.getFunctionCall();
        if (functionCall != null) {
            Optional<ChatMessage> functionResponseMessage = functionExecutor.executeAndConvertToMessageSafely(functionCall);
            if (functionResponseMessage.isPresent()) {
                logger.debug(functionResponseMessage.toString());
                messages.add(functionResponseMessage.get());
                responseMessage = service.createChatCompletion(buildCompletionRequest()).getChoices().get(0).getMessage();
                logger.debug(responseMessage.toString());
                messages.add(responseMessage);
            } else {
                logger.error("Function call failed");
            }
        }
        return responseMessage.getContent();
    }

    public List<String> getItemsList() {
        return itemsList;
    }
}
