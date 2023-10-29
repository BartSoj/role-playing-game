package example.org.service.gamelogic;

import example.org.dto.GameStatus;
import example.org.service.chatcompletion.GetNextTurn;
import example.org.service.chatcompletion.GetNextTurnRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GameLogicService {

    public abstract GameStatus initGameStatus();

    public abstract String getSystemMessage();

    public GetNextTurnRequest convertToRequest(GameStatus gameStatus) {
        GetNextTurnRequest request = new GetNextTurnRequest();
        request.time = gameStatus.getTime();
        request.day = gameStatus.getDay();
        request.xp = gameStatus.getXp();
        request.level = gameStatus.getLevel();
        request.hp = gameStatus.getHp();
        request.inventory = gameStatus.getInventory();
        request.quests = gameStatus.getQuests();
        return request;
    }

    public GameStatus updateGameStatus(GameStatus gameStatus, GetNextTurn nextTurn) {
        GameStatus newGameStatus = new GameStatus();
        newGameStatus.setGameMode(gameStatus.getGameMode());
        newGameStatus.setRound(gameStatus.getRound() + 1);
        newGameStatus.setTime(nextTurn.timeOfDay);
        newGameStatus.setDay(nextTurn.dayNumber);
        int xp = gameStatus.getXp() + nextTurn.gainedExperiencePoints;
        if (xp > 1000) {
            newGameStatus.setXp(xp - 1000);
            newGameStatus.setLevel(gameStatus.getLevel() + 1);
            newGameStatus.setHp(20);
        } else {
            newGameStatus.setXp(xp);
            newGameStatus.setLevel(gameStatus.getLevel());
        }
        newGameStatus.setHp(nextTurn.hp);
        newGameStatus.setCombatMode(nextTurn.engagedInCombat);
        newGameStatus.setInventory(nextTurn.inventory);
        newGameStatus.setCompletedQuests(gameStatus.getCompletedQuests());
        for (int i = 0; i < nextTurn.quests.size(); i++) {
            if (nextTurn.questCompleted.get(i)) {
                newGameStatus.getCompletedQuests().add(nextTurn.quests.get(i));
            } else {
                newGameStatus.getQuests().add(nextTurn.quests.get(i));
            }
        }
        newGameStatus.setMessages(gameStatus.getMessages());
        return newGameStatus;
    }
}
