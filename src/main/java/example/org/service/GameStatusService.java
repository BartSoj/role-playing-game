package example.org.service;

import example.org.dto.GameStatus;
import example.org.service.chatcompletion.ChatCompletionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameStatusService {
    ChatCompletionService completionService;

    public GameStatusService() {
        completionService = new ChatCompletionService();
    }

    public GameStatus getNewGameStatus() {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setRound(1);
        gameStatus.setTime("08:00");
        gameStatus.setDay("1");
        gameStatus.setWeather("sunny");
        gameStatus.setXp("100");
        gameStatus.getMessages().add("Welcome, brave adventurer, to the mystical and perilous city of Dundee. In this epic role-playing game, you will embark on a heroic quest inspired by the Tales from the Kingdom of Fife.\n" +
                "\n" +
                "The land of Fife is in grave danger, and the prophecy foretells a dark and ominous fate for the city of Dundee. The sorcerer Zargothrax has risen, wielding dark magic that has corrupted once-noble unicorns and turned them into instruments of destruction. As dawn breaks, he leads his unholy army on a merciless invasion of Dundee, raining fireballs and chaos upon the unsuspecting town.");
        return gameStatus;
    }


    public GameStatus getNextGameStatus(GameStatus gameStatus, String message) {
        gameStatus.getMessages().add(message);
        return completionService.getCompletion(gameStatus);
    }
}
