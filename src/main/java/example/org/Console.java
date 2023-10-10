package example.org;

import example.org.dto.GameStatus;
import example.org.service.GameStatusService;

import java.util.Scanner;

public class Console {
    public static void main(String[] args) {
        GameStatusService gameStatusService = new GameStatusService();
        GameStatus gameStatus = gameStatusService.getNewGameStatus();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!"exit".equals(input)) {
            input = scanner.nextLine();
            gameStatus = gameStatusService.getNextGameStatus(gameStatus, input);
            System.out.println("RESPONSE: " + gameStatus.getMessages().get(gameStatus.getMessages().size() - 1));
        }
    }
}
