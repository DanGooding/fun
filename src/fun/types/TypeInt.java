package fun.types;

import java.util.HashSet;
import java.util.Set;

public class TypeInt extends Type {

    @Override
    public Type applySubstitution(Substitution s) {
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
    UnifyingAction unifyWithInt(TypeInt i) {
        return UnifyingAction.doNothing();
    }

    @Override
    public String toString() {
        return "Int";
    }
}
