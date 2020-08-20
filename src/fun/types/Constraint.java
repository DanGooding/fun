package fun.types;

import java.util.Map;
import java.util.Set;

class Constraint implements Substitutable<Constraint> {
    private Type left;
    private Type right;
    Constraint(Type left, Type right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Constraint applySubstitution(Substitution s) {
        return new Constraint(left.applySubstitution(s), right.applySubstitution(s));
    }

    @Override
    public Set<String> freeTypeVariables() {
        Set<String> free = left.freeTypeVariables();
        free.addAll(right.freeTypeVariables());
        return free;
    }

    public Type getLeft() {
        return left;
    }

    public Type getRight() {
        return right;
    }
}