package example.org.service.chatcompletion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public class GetNextTurn {
    @JsonPropertyDescription("Description of this turn according to nextTurnDescription")
    @JsonProperty(required = true)
    public String description;
    @JsonPropertyDescription("Estimated number of hours the user action would take")
    @JsonProperty(required = true)
    public int hoursConsumed;
    @JsonPropertyDescription("New experience points gained by the user, value dependent on user action")
    @JsonProperty(required = true)
    public int gainedExperiencePoints;
    @JsonPropertyDescription("New hp points. Should change depending on user action")
    @JsonProperty(required = true)
    public int hp;
    @JsonPropertyDescription("True if the user is engaged in combat. If all the enemies are defeated, this should be false")
    public boolean engagedInCombat;
    @JsonPropertyDescription("Items in inventory")
    @JsonProperty(required = true)
    public List<String> inventory;

    @JsonPropertyDescription("All the quests thah the player has to complete")
    @JsonProperty(required = true)
    public List<String> quests;

    @JsonPropertyDescription("All the quests that the player has completed")
    @JsonProperty(required = true)
    public List<String> completedQuests;
}