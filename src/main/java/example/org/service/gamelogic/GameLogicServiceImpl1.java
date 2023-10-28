package example.org.service.gamelogic;

import example.org.dto.GameStatus;
import example.org.service.chatcompletion.GetNextTurnRequest;

import java.util.List;

public class GameLogicServiceImpl1 extends GameLogicService {

    private static final String GAME_NAME = "Battlefield";

    private final String SYSTEM_MESSAGE = """
            Please perform the function of a text adventure game, following the rules listed below:
            Character Stats:
            Hour of the day, number of the day, user XP, user level, user HP, combat mode, inventory, active quests, and completed quests are all important properties that the AI needs to manage.
            Time and Day:
            The game's world should have a dynamic day-night cycle. The AI should keep track of the current time and day, which can influence various events and interactions.
            User XP and Level:
            The AI should award XP for completing quests and defeating enemies. The AI should adjust the user's level accordingly and inform the player of any new abilities or advantages gained.
            User HP:
            User HP (Hit Points) represent the character's health. In combat, the AI should calculate damage received and update the user's HP accordingly. The player can with the use of items.
            Combat Mode:
            The AI should switch combat mode to "true" when the player is engaged in combat. During combat, the AI should provide the player with options for attacking, defending, or using special abilities. When combat ends, set combat mode to "false."
            Inventory:
            Maintain a list of items and equipment in the player's inventory. Allow the player to view, equip, use, or drop items. Inventory management should influence the character's stats and abilities.
            Quests:
            The AI should keep track of available quests. Never add new quests. The quest is completed if the player messages indicate that player completed the quest in any degree."
            User Input Handling:
            The AI should be able to process a wide range of user inputs. It should recognize and respond to commands related to movement, interaction with objects, initiating combat, using items, accepting quests, and more.
            Narrative and Descriptions:
            The AI should be able to generate rich descriptions of the game world, characters, and events to immerse the player in the story. Descriptions should adapt based on the time of day, location, and player progress.
            Conversational AI:
            Enable the AI to engage in dialogues with non-player characters (NPCs) or provide hints and guidance to the player. It should be able to generate responses that fit the personalities and roles of different NPCs.
            Random Events:
            Introduce random events, encounters, or choices to keep the gameplay unpredictable and exciting. The AI should respond to these events based on the player's choices.
            Restrictions:
            The response should consist solely of description of the game world, the player's surroundings, and the player's actions. The AI should not provide any information about the game's mechanics, such as the player's stats, the number of enemies, or the amount of damage dealt.
            The response short and concise, it must be 2 sentences long. The AI should not provide a wall of text.
            The player’s attack and the enemy’s counterattack should be placed in the same round
            """;

    public static String getGameName() {
        return GAME_NAME;
    }

    public GameStatus initGameStatus() {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setGameMode(GAME_NAME);
        gameStatus.setRound(1);
        gameStatus.setTime("Early morning");
        gameStatus.setDay(1);
        gameStatus.setXp(100);
        gameStatus.setLevel(1);
        gameStatus.setHp(20);
        gameStatus.setCombatMode(false);
        gameStatus.getInventory().add("Sword");
        gameStatus.getInventory().add("Shield");
        gameStatus.setQuests(List.of("Slaughter 3 enemies"));
        gameStatus.getMessages().add("You wake up in the middle of the battle field. You are surrounded by enemies. They can attack you at any moment.");
        return gameStatus;
    }

    public String getSystemMessage() {
        return SYSTEM_MESSAGE;
    }

    private String getTurnCircumstances(GameStatus gameStatus) {
        if (gameStatus.getHp() <= 0) {
            return "You have died. You have lost the game.";
        }
        if (gameStatus.getQuests().isEmpty()) {
            return "You have completed the quest. You have won the game.";
        }
        if (gameStatus.isCombatMode()) {
            return "Hostile forces are attacking the player. Precisely describe the enemy attack, what weapon or spell enemy used to attack and the number of hp point lost by the player. Do not allow player to avoid the attack. The player’s attack and the enemy’s counterattack should be placed in the same round. The combat ends only when enemies are killed";
        } else {
            return "Hostile forces are approaching. Engage player in the combat. The player can not avoid combat. If the player is engaged in combat, the player should be attacked every turn. The combat ends only when enemies are killed.";
        }
    }

    @Override
    public GetNextTurnRequest convertToRequest(GameStatus gameStatus) {
        GetNextTurnRequest nextTurnRequest = super.convertToRequest(gameStatus);
        nextTurnRequest.nextTurnDescription = getTurnCircumstances(gameStatus);
        return nextTurnRequest;
    }
}
