package fun.eval;

import java.util.HashMap;
import java.util.Map;

public class Environment<T> {  // TODO: make immutable

    private Map<String, T> bindings;

    public Environment(Map<String, T> bindings) {
        this.bindings = new HashMap<>(bindings); // must create a mutable copy !
    }

    public Environment(Environment<T> env) {
        this(env.bindings);
    }

    public Environment() {
        this(Map.of());
    }

    /**
     * mutating update: put a new binding
     */
    public void bind(String name, T value) {
        bindings.put(name, value);
    }

    /**
     * return the value associated with `name`, or null if `name` is unbound
     */
    public T lookup(String name) {
         return bindings.get(name);
    }

    public boolean hasName(String name) {
        return bindings.containsKey(name);
    }

    public Map<String, T> getBindings() {
        return new HashMap<>(bindings);
    }

}
