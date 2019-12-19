import java.util.HashMap;
import java.util.Map;

// TODO: should this be immutable ?
// literally just use a map ?

// for laziness, store ast not value, then evaluate on demand (caching result)
// this definitely requires immutability (?)
// wont we end up storing the environment with every thunk ?
// use a thunk wrapper class, or manage that here ?

class Environment {

    private Map<String, Value> bindings;

    Environment() {
        bindings = new HashMap<>();
    }

    Environment(Environment env) {
        this.bindings = new HashMap<>(env.bindings);
    }

    Value lookup(String name) {
         return bindings.get(name);
    }

    boolean hasName(String name) {
        return bindings.containsKey(name);
    }

    void bind(String name, Value value) {
        bindings.put(name, value);
    }

}
