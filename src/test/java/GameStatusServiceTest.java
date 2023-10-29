import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicService;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameStatusServiceTest {

    @Test
    public void testGetNewGameStatus() {
        GameLogicService gameLogicService = new GameLogicServiceImpl1();
        GameStatus gameStatus1 = gameLogicService.initGameStatus();
        GameStatusService gameStatusService = new GameStatusService(gameLogicService);
        GameStatus gameStatus2 = gameStatusService.getNewGameStatus();
        Assertions.assertNotNull(gameStatus1);
        Assertions.assertNotNull(gameStatus2);
        Assertions.assertEquals(gameStatus1, gameStatus2);
    }
}
