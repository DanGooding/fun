package fun.types;

import java.util.HashMap;
import java.util.Map;

/**
 * maps variable names to fresh names, in order of appearance
 */
public class VariableNameRefresher {
    private final NameSource nameSource;
    private final Map<String, String> nameMappings;

    public VariableNameRefresher() {
        nameSource = new NameSource();
        nameMappings = new HashMap<>();
    }

    String refreshName(String name) {
        if (nameMappings.containsKey(name)) {
            return nameMappings.get(name);
        }
        String newName = nameSource.freshName();
        nameMappings.put(name, newName);
        return newName;
    }

}
