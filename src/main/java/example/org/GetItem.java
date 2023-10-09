package example.org;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

public class GetItem {
    public static class GetWeapon {
        @JsonPropertyDescription("The name of the weapon")
        @Required
        public String name;
    }
}
