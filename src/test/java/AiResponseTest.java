import example.org.AiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class AiResponseTest {
    @Test
    public void testResponse() {
        AiResponse aiResponse = new AiResponse();
        String result = aiResponse.getResponse("Hello");
        Assertions.assertNotNull(result);
        aiResponse.getResponse("get knife");
        Assertions.assertEquals(List.of("knife"), aiResponse.getItemsList());
    }
}
