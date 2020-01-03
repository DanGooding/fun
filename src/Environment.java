import java.util.HashMap;
import java.util.Map;

class Environment {

    private final Map<String, Value> bindings;

    // the enclosing environment
    private final Environment outer;

    Environment(Map<String, Value> bindings, Environment outer) {
        this.bindings = new HashMap<>(bindings); // must create a mutable copy !
        this.outer = outer;
    }

    Environment(Environment outer) {
        this(Map.of(), outer);
    }

    Environment() {
        this(Map.of(), null);
    }

    /**
     * mutating update
     */
    void bind(String name, Value value) {
        bindings.put(name, value);
    }

    Value lookup(String name) throws EvaluationException {
         if (bindings.containsKey(name)) {
             return bindings.get(name);
         }
         if (outer == null) {
             throw new EvaluationException(String.format("unbound variable '%s'", name));
         }
         return outer.lookup(name);
    }

    boolean hasName(String name) {
        // exploits short circuiting of || and &&
        return bindings.containsKey(name) || (outer != null && outer.hasName(name));
    }

}
