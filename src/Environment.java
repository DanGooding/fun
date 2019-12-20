import java.util.HashMap;
import java.util.Map;

class Environment {

    private Map<String, Value> bindings;

    Environment() {
        bindings = new HashMap<>();
    }

    /**
     * shallow copy constructor
     */
    Environment(Environment env) {
        // assumes `Value`s are immutable, doesn't deepcopy
        this.bindings = new HashMap<>(env.bindings);
    }

    /**
     * mutating update
     */
    void bind(String name, Value value) {
        bindings.put(name, value);
    }

    /**
     * return a copy of this environment,
     * adding the binding (value, name) to it
     */
    Environment withBinding(String name, Value value) {
        Environment newEnv = new Environment(this);
        newEnv.bind(name, value);
        return newEnv;
    }

    Value lookup(String name) {
         return bindings.get(name);
    }

    boolean hasName(String name) {
        return bindings.containsKey(name);
    }

}
