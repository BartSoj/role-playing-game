package example.org.service;

import example.org.dto.GameStatus;
import example.org.service.chatcompletion.ChatCompletionService;
import example.org.service.gamelogic.GameLogicService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameStatusService {
    private final GameLogicService gameLogicService;
    private final ChatCompletionService completionService;

    public GameStatusService(GameLogicService gameLogicService) {
        this.gameLogicService = gameLogicService;
        completionService = new ChatCompletionService(gameLogicService);
    }

    public GameStatus getNewGameStatus() {
        return gameLogicService.initGameStatus();
    }


    public Flowable<String> getFlowableDescription(GameStatus gameStatus) {
        return completionService.getFlowableDescription(gameStatus);
    }

    public GameStatus getNextGameStatus(GameStatus gameStatus) {
        return completionService.getNewGameStatus(gameStatus);
    }
}
