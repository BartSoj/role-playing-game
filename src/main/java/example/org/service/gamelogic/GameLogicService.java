package example.org.service.gamelogic;

import example.org.dto.GameStatus;
import example.org.service.chatcompletion.GetNextTurn;
import example.org.service.chatcompletion.GetNextTurnRequest;

import java.util.List;

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
        newGameStatus.setRound(gameStatus.getRound() + 1);
        int hour = gameStatus.getTime() + nextTurn.hoursConsumed;
        if (hour > 24) {
            newGameStatus.setTime(hour - 24);
            newGameStatus.setDay(gameStatus.getDay() + 1);
        } else {
            newGameStatus.setTime(hour);
            newGameStatus.setDay(gameStatus.getDay());
        }
        int xp = gameStatus.getXp() + nextTurn.gainedExperiencePoints;
        if (xp > 1000) {
            newGameStatus.setXp(xp - 1000);
            newGameStatus.setLevel(gameStatus.getLevel() + 1);
        } else {
            newGameStatus.setXp(xp);
            newGameStatus.setLevel(gameStatus.getLevel());
        }
        newGameStatus.setHp(nextTurn.hp);
        newGameStatus.setCombatMode(nextTurn.engagedInCombat);
        newGameStatus.setInventory(nextTurn.inventory);
        newGameStatus.setQuests(nextTurn.quests);
        List<String> completedQuests = gameStatus.getCompletedQuests();
        completedQuests.addAll(nextTurn.completedQuests);
        newGameStatus.setCompletedQuests(completedQuests);
        newGameStatus.setMessages(gameStatus.getMessages());
        return newGameStatus;
    }
}
