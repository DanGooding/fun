package fun.values;

import fun.eval.EvaluationException;
import fun.eval.Thunk;

public class ListConsValue implements ConstantValue {

    private final Thunk head; // TODO: is this in a thunk?
    private final Thunk tail;

    public ListConsValue(Thunk head, Thunk tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void fullyForce() throws EvaluationException {
        head.force().fullyForce();
        tail.force().fullyForce();
    }

    @Override
    public String toString() {
        return String.format("(%s : %s)", head, tail);
    }
}
