package example.org;

import example.org.dto.GameStatus;
import example.org.service.GameStatusService;
import example.org.service.gamelogic.GameLogicServiceImpl1;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        GameStatusService gameStatusService = new GameStatusService(new GameLogicServiceImpl1());
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        Scanner scanner = new Scanner(System.in);
        System.out.println("RESPONSE: " + gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
        System.out.println("GAME STATUS: " + gameStatus);
        String input = "";
        while (!"exit".equals(input)) {
            input = scanner.nextLine();
            gameStatus.getMessages().add(input);
            gameStatus = gameStatusService.getNextGameStatus(gameStatus);
            System.out.println("RESPONSE: " + gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
            System.out.println("GAME STATUS: " + gameStatus);
        }
    }
}
