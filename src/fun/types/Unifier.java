package fun.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Unifier {

    public static Substitution unifyTypes(Type a, Type b) throws UnificationFailureException {

        List<Constraint> constraints = new ArrayList<>();
        constraints.add(new Constraint(a, b));

        Substitution unifier = new Substitution();

        while (constraints.size() > 0) {

            // TODO: does order of push/pop matter?

            Constraint constraint = constraints.remove(constraints.size() - 1);


            UnifyingAction action = constraint.getLeft().dispatchUnify(constraint.getRight());

            // add any additional constraints to be solved
            constraints.addAll(action.getConstraints());

            if (action.hasSubstitution()) {
                Substitution s = action.getSubstitution();

                constraints = constraints.stream().map(c -> c.applySubstitution(s)).collect(Collectors.toList());
                unifier = unifier.compose(s);
            }
        }

        return unifier;
    }

}
