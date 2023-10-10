import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Scanner;


public class ChatCompletionServiceTest {
    @Test
    public void testGetCompletion() {
        GameStatusService gameStatusService = new GameStatusService();
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        gameStatus = gameStatusService.getNextGameStatus(gameStatus, "take the apple");
        Assertions.assertEquals("take the apple", gameStatus.getMessages().get(gameStatus.getMessages().size() - 2));
        Assertions.assertNotNull(gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
        Assertions.assertEquals(List.of("apple"), gameStatus.getInventory());
    }

}
