package example.org.service.chatcompletion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public class GetNextTurn {
    @JsonPropertyDescription("one sentence description of what can happen next in the game based on the current state and user input")
    @JsonProperty(required = true)
    public String description;
    @JsonPropertyDescription("Time period of the day, should change every time")
    @JsonProperty(required = true)
    public String time;

    @JsonPropertyDescription("Day number should increase from time to time")
    @JsonProperty(required = true)
    public String day;

    @JsonPropertyDescription("Weather condition should change every few rounds")
    @JsonProperty(required = true)
    public String weather;

    @JsonPropertyDescription("Experience points, increases when user does something good")
    @JsonProperty(required = true)
    public String xp;
    @JsonPropertyDescription("Items in inventory")
    @JsonProperty(required = true)
    public List<String> inventory;
}