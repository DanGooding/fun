package fun.eval;

import java.util.HashMap;
import java.util.Map;

public class Environment {  // TODO: make immutable

    private final Map<String, Thunk> bindings;

    // the enclosing environment
    private final Environment outer;

    public Environment(Map<String, Thunk> bindings, Environment outer) {
        this.bindings = new HashMap<>(bindings); // must create a mutable copy !
        this.outer = outer;
    }

    public Environment(Environment outer) {
        this(Map.of(), outer);
    }

    public Environment() {
        this(Map.of(), null);
    }

    /**
     * mutating update
     */
    public void bind(String name, Thunk thunk) {
        bindings.put(name, thunk);
    }

    public Thunk lookup(String name) throws EvaluationException {
         if (bindings.containsKey(name)) {
             return bindings.get(name);
         }
         if (outer == null) {
             throw new EvaluationException(String.format("unbound variable '%s'", name));
         }
         return outer.lookup(name);
    }

    public boolean hasName(String name) {
        // exploits short circuiting of || and &&
        return bindings.containsKey(name) || (outer != null && outer.hasName(name));
    }

}
