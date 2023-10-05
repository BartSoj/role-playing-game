package example.org;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AiResponse {
    private final OpenAiService service;
    private final List<ChatMessage> messages;

    public AiResponse() {
        String token = System.getenv("OPENAI_API_KEY");
        service = new OpenAiService(token);
        messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);
    }

    public String getResponse(String message) {
        messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        String result = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), result));
        return result;
    }
}
