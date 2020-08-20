package fun.types;

import java.util.Set;

interface Substitutable<T> {
    /**
     * apply the substitution s, producing a new T with free type variables substituted
     */
    T applySubstitution(Substitution s);

    /**
     * return the free (unbound by forall.) type variables
     */
    Set<String> freeTypeVariables();
}
