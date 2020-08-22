package fun.types;

import java.util.HashSet;
import java.util.Set;

public class TypeBool extends Type {

    @Override
    public TypeBool applySubstitution(Substitution s) {
        return this;
    }

    @Override
    public Set<String> freeTypeVariables() {
        return new HashSet<>();
    }

    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        return t.unifyWithBool(this);
    }

    @Override
    UnifyingAction unifyWithBool(TypeBool b) {
        return UnifyingAction.doNothing();
    }

    @Override
    TypeBool refreshVariableNames(VariableNameRefresher v) {
        return new TypeBool();
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
        return "Bool";
    }
}

