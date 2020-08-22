package fun.types;

import fun.ast.ASTNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Inferer {

    private final NameSource nameSource;
    private Substitution globalSubstitution;

    Inferer() {
        nameSource = new NameSource();
        globalSubstitution = new Substitution();
    }

    public TypeVariable freshVariable() {
        return new TypeVariable(nameSource.freshName());
    }

    public void unify(Type a, Type b) throws UnificationFailureException {
        Substitution s = Unifier.unifyTypes(a, b);
        globalSubstitution = globalSubstitution.compose(s);
    }

    /**
     * generalise a type by closing over the free type variables with a forall
     */
    public Scheme generalise(Type t, TypeEnvironment env) {
        Set<String> free = t.freeTypeVariables();
        free.removeAll(env.freeTypeVariables());
        return new Scheme(free, t);
    }

    /**
     * replace the quantified variables with fresh ones
     */
    public Type instantiate(Scheme scheme) {
        Scheme explicitScheme = scheme.applySubstitution(globalSubstitution);

        List<String> quantified = explicitScheme.getQuantified();
        Map<String, Type> rename = new HashMap<>();
        for (String name : quantified) {
            rename.put(name, new TypeVariable(nameSource.freshName()));
        }
        return explicitScheme.getBody().applySubstitution(new Substitution(rename));
    }


    // TODO: work out a good use model & interface for this class
    // maybe annotate the whole tree ? (requires a second pass)

    public static Scheme inferType(ASTNode expr) throws TypeErrorException {
        Inferer inferer = new Inferer();

        TypeEnvironment outerEnv = new TypeEnvironment();
        Type type = expr.inferType(inferer, outerEnv);

        // FIXME: if the type is used elsewhere, type variables must be _globally_ unique
        return inferer.generalise(type.applySubstitution(inferer.globalSubstitution), outerEnv);
    }

}
