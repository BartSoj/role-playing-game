package example.org;

import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;

import java.util.Scanner;

public class Console {
    private static void printGameStatus(GameStatus gameStatus) {
        System.out.println("Game Status:");
        System.out.println("Round: " + gameStatus.getRound());
        System.out.println("Time: " + gameStatus.getTime());
        System.out.println("Day: " + gameStatus.getDay());
        System.out.println("XP: " + gameStatus.getXp());
        System.out.println("Level: " + gameStatus.getLevel());
        System.out.println("HP: " + gameStatus.getHp());
        System.out.println("Inventory: " + gameStatus.getInventory());
        System.out.println("Quests: " + gameStatus.getQuests());
        System.out.println("Completed Quests: " + gameStatus.getCompletedQuests());
    }

    public static void main(String[] args) {
        GameStatusService gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        Scanner scanner = new Scanner(System.in);
        System.out.print("First message: ");
        for (String message : gameStatus.getMessages()) {
            System.out.print(message);
        }
        printGameStatus(gameStatus);
        String input = "";
        while (!"exit".equals(input)) {
            System.out.print("Input: ");
            input = scanner.nextLine();
            gameStatus.getMessages().add(input);
            System.out.print("Response: ");
            StringBuilder response = new StringBuilder();
            gameStatusService.getFlowableDescription(gameStatus).doOnNext(message -> {
                System.out.print(message);
                response.append(message);
            }).blockingSubscribe();
            System.out.println();
            gameStatus.getMessages().add(response.toString());
            printGameStatus(gameStatus);
        }
    }
}
