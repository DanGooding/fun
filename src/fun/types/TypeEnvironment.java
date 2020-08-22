package fun.types;

// TODO: move into top level / utils package?
import fun.eval.Environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * maps variable names to type schemes
 */
public class TypeEnvironment extends Environment<Scheme> implements Substitutable<TypeEnvironment> {

    public TypeEnvironment() {
        super();
    }

    public TypeEnvironment(TypeEnvironment other) {
        super(other);
    }

    @Override
    public TypeEnvironment applySubstitution(Substitution s) {
        TypeEnvironment newEnv = new TypeEnvironment();
        Map<String, Scheme> currentBindings = getBindings();
        for (Map.Entry<String, Scheme> e : currentBindings.entrySet()) {
            newEnv.bind(
                e.getKey(),
                e.getValue().applySubstitution(s)
            );
        }
        return newEnv;
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = new HashSet<>();
        for (Scheme scheme : getBindings().values()) {
            free.addAll(scheme.freeTypeVariables());
        }
        return free;
    }
}
