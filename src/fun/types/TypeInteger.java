package fun.types;

import java.util.HashSet;
import java.util.Set;

public class TypeInteger extends Type {

    @Override
    public TypeInteger applySubstitution(Substitution s) {
        return this;
    }

    @Override
    public Set<String> freeTypeVariables() {
        return new HashSet<>();
    }

    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        return t.unifyWithInt(this);
    }

    @Override
    UnifyingAction unifyWithInt(TypeInteger i) {
        return UnifyingAction.doNothing();
    }

    @Override
    public TypeInteger refreshVariableNames(VariableNameRefresher v) {
        return new TypeInteger();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Int";
    }
}
