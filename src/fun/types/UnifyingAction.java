package fun.types;

import java.util.List;
import java.util.Map;

/**
 * represents how to unify two types:
 * by a substitution, by further unification, or both
 */
class UnifyingAction {
    private Substitution substitution;
    private List<Constraint> constraints;

    UnifyingAction(Substitution substitution, List<Constraint> constraints) {
        this.substitution = substitution;
        this.constraints = List.copyOf(constraints);
    }

    static UnifyingAction substitute(String name, Type t) {
        return new UnifyingAction(new Substitution(name, t), List.of());
    }

    static UnifyingAction furtherUnify(Constraint... constraints) {
        return new UnifyingAction(null, List.of(constraints));
    }
    static UnifyingAction furtherUnify(List<Constraint> constraints) {
        return new UnifyingAction(null, constraints);
    }

    static UnifyingAction doNothing() {
        return new UnifyingAction(null, List.of());
    }

    boolean hasSubstitution() {
        return substitution != null;
    }

    Substitution getSubstitution() {
        return substitution;
    }

    List<Constraint> getConstraints() {
        return constraints;
    }
}
