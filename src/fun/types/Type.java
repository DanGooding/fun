package fun.types;

public abstract class Type implements Substitutable<Type> {

    abstract UnifyingAction dispatchUnify(Type t) throws UnificationFailureException;

    UnifyingAction unifyWithVariable(TypeVariable tv) throws UnificationFailureException {
        return tv.unifyWithAny(this);
    }

    UnifyingAction unifyWithArrow(TypeArrow ar) throws UnificationFailureException {
        return unifyWithAny(ar);
    }
    UnifyingAction unifyWithInt(TypeInt i) throws UnificationFailureException {
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
}
