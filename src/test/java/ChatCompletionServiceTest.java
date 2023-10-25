import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class ChatCompletionServiceTest {
    @Test
    public void testGetCompletion() {
        GameStatusService gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        gameStatus.getMessages().add("take the apple");
        gameStatus = gameStatusService.getNextGameStatus(gameStatus);
        Assertions.assertEquals("take the apple", gameStatus.getMessages().get(gameStatus.getMessages().size() - 2));
        Assertions.assertNotNull(gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
        Assertions.assertEquals(List.of("apple"), gameStatus.getInventory());
    }

}
