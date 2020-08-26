package fun.types;

import java.util.Objects;
import java.util.Set;

/**
 * the type of functions: left -> right
 */
public class TypeArrow extends Type {
    private final Type left;
    private final Type right;

    public TypeArrow(Type left, Type right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public TypeArrow applySubstitution(Substitution s) {
        return new TypeArrow(left.applySubstitution(s), right.applySubstitution(s));
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = left.freeTypeVariables();
        free.addAll(right.freeTypeVariables());
        return free;
    }

    @Override
    UnifyingAction dispatchUnify(Type t) throws UnificationFailureException {
        return t.unifyWithArrow(this);
    }

    @Override
    UnifyingAction unifyWithArrow(TypeArrow ar) {
        return UnifyingAction.furtherUnify(
            new Constraint(left, ar.left),
            new Constraint(right, ar.right)
        );
    }

    @Override
    public TypeArrow refreshVariableNames(VariableNameRefresher v) {
        return new TypeArrow(
            left.refreshVariableNames(v),
            right.refreshVariableNames(v)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeArrow typeArrow = (TypeArrow) o;
        return left.equals(typeArrow.left) &&
            right.equals(typeArrow.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return String.format("(%s) -> (%s)", left, right);
    }

    @Override
    public String prettyPrint() {
        return String.format("%s -> %s", left.prettyPrintAsArrowLeftChild(), right.prettyPrint());
    }

    @Override
    public String prettyPrintAsArrowLeftChild() {
        return String.format("(%s)", prettyPrint());
    }
}
