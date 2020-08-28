package fun.types;

import java.util.Objects;
import java.util.Set;

public class TypeList extends Type {

    private final Type element;

    public TypeList(Type element) {
        this.element = element;
    }

    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        return t.unifyWithList(this);
    }

    @Override
    UnifyingAction unifyWithList(TypeList l) throws UnificationFailureException {
        return element.dispatchUnify(l.element);
//        return UnifyingAction.furtherUnify(new Constraint(element, l.element));
    }

    @Override
    public TypeList refreshVariableNames(VariableNameRefresher v) {
        return new TypeList(element.refreshVariableNames(v));
    }

    @Override
    public TypeList applySubstitution(Substitution s) {
        return new TypeList(element.applySubstitution(s));
    }

    @Override
    public Set<String> freeTypeVariables() {
        return element.freeTypeVariables();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeList typeList = (TypeList) o;
        return element.equals(typeList.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }

    @Override
    public String toString() {
        return String.format("[%s]", element);
    }

    @Override
    public String prettyPrint() {
        return String.format("[%s]", element.prettyPrint());
    }
}
