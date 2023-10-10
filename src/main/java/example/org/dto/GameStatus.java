package example.org.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class GameStatus {
    private int round;
    private String time;
    private String day;
    private String weather;
    private String xp;
    private List<String> inventory = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
}
