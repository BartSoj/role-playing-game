import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ChatCompletionServiceTest {

    @Test
    public void testGetFlowableDescription() {
        GameStatusService gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        gameStatus.getMessages().add("I look around");
        gameStatusService.getFlowableDescription(gameStatus)
                .doOnNext(Assertions::assertNotNull)
                .blockingSubscribe();
    }

    @Test
    public void testGetCompletion() {
        GameStatusService gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        gameStatus.getMessages().add("I look around");
        StringBuilder description = new StringBuilder();
        gameStatusService.getFlowableDescription(gameStatus)
                .doOnNext(description::append)
                .blockingSubscribe();
        gameStatus.getMessages().add(description.toString());
        gameStatus = gameStatusService.getNextGameStatus(gameStatus);
        Assertions.assertEquals("I look around", gameStatus.getMessages().get(gameStatus.getMessages().size() - 2));
        Assertions.assertNotNull(gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
        Assertions.assertEquals(2, gameStatus.getRound());
    }

}
