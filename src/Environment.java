import java.util.HashMap;
import java.util.Map;

// TODO: should this be immutable ?
// literally just use a map ?

// for laziness, store ast not value, then evaluate on demand (caching result)

class Environment {

    private Map<String, ConstantValue> bindings;

    Environment() {
        bindings = new HashMap<>();
    }

    Environment(Environment env) {
        this.bindings = new HashMap<>(env.bindings);
    }

    ConstantValue lookup(String name) {
         return bindings.get(name);
    }

    boolean hasName(String name) {
        return bindings.containsKey(name);
    }

    void bind(String name, ConstantValue value) {
        bindings.put(name, value);
    }

}
