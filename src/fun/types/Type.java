package fun.types;

public abstract class Type implements TypeLike {

    /**
     * call the relevant unifyWithX method on t, depending on the type of this
     */
    abstract UnifyingAction dispatchUnify(Type t) throws UnificationFailureException;

    UnifyingAction unifyWithVariable(TypeVariable tv) throws UnificationFailureException {
        return tv.unifyWithAny(this);
    }

    UnifyingAction unifyWithArrow(TypeArrow ar) throws UnificationFailureException {
        return unifyWithAny(ar);
    }
    UnifyingAction unifyWithInt(TypeInteger i) throws UnificationFailureException {
        return unifyWithAny(i);
    }
    UnifyingAction unifyWithBool(TypeBool b) throws UnificationFailureException {
        return unifyWithAny(b);
    }
    UnifyingAction unifyWithTuple(TypeTuple t) throws UnificationFailureException {
        return unifyWithAny(t);
    }

    UnifyingAction unifyWithAny(Type t) throws UnificationFailureException {
        throw new UnificationFailureException(this, t);
    }

    /**
     * construct a new equivalent Type, with type variables named consecutively in order of appearance
     */
    @Override
    public abstract Type refreshVariableNames(VariableNameRefresher v);

    @Override
    public Type instantiate(Inferer inferer) {
        return this;
    }

    @Override
    public abstract Type applySubstitution(Substitution s);


    public abstract String prettyPrint();

    public String prettyPrintAsArrowLeftChild() {
        return prettyPrint();
    }

}
