package fun.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * an immutable mapping from type variables to other types,
 * that can be applied to Substitutable.
 */
public class Substitution {

    private Map<String, Type> subst;

    Substitution(Map<String, Type> subst) {
        this.subst = Map.copyOf(subst);
    }

    Substitution(String var, Type t) {
        this(Map.of(var, t));
    }

    Substitution() {
        this(Map.of());
    }

    /**
     * get the type a given variable is substituted by
     */
    Type get(String varName) {
        if (subst.containsKey(varName)) {
            return subst.get(varName);
        }
        return new TypeVariable(varName);
    }

    /**
     * Combines two substitutions, the result being equivalent
     * to first applying `this` then `other`
     */
    Substitution compose(Substitution other) {
        // apply `other` to all replacements in `this`
        // add `other`'s map to this

        Map<String, Type> result = new HashMap<>();
        for (Map.Entry<String, Type> e : subst.entrySet()) {
            result.put(
                e.getKey(),
                e.getValue().applySubstitution(other)
            );
        }
        result.putAll(other.subst);
        return new Substitution(result);
    }

    /**
     * returns a copy, but with any substitutions of the
     * named type variables removed
     */
    Substitution without(List<String> names) {
        Map<String, Type> result = new HashMap<>(subst);
        for (String name : names) {
            result.remove(name);
        }
        return new Substitution(result);
    }

    Map<String, Type> toMap() {
        return Map.copyOf(this.subst);
    }

}
