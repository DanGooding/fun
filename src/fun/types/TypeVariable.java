package fun.types;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TypeVariable extends Type {
    private final String name;

    public TypeVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type applySubstitution(Substitution s) {
        return s.get(name);
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = new HashSet<>();
        free.add(name);
        return free;
    }



    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        // don't care what the other type is here
        return t.unifyWithVariable(this);
    }

    @Override
    UnifyingAction unifyWithVariable(TypeVariable tv) throws UnificationFailureException {
        return unifyWithAny(tv);
    }

    @Override
    UnifyingAction unifyWithAny(Type t) throws UnificationFailureException {
        // occurs check
        if (t.freeTypeVariables().contains(name)) {
            if (this.equals(t)) {
                return UnifyingAction.doNothing();
            }
            throw new UnificationFailureException(this, t);
        }

        return UnifyingAction.substitute(name, t);
    }

    @Override
    public TypeVariable refreshVariableNames(VariableNameRefresher v) {
        return new TypeVariable(v.refreshName(name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeVariable that = (TypeVariable) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String prettyPrint() {
        return name;
    }
}