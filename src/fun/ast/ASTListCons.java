package fun.ast;

import fun.eval.Environment;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.types.TypeErrorException;
import fun.values.ListConsValue;
import fun.values.Value;

public class ASTListCons extends ASTNode {

    private final ASTNode head;
    private final ASTNode tail;

    public ASTListCons(ASTNode head, ASTNode tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Value evaluate(Environment<Thunk> env) {
        return new ListConsValue(
            new Thunk(head, env),
            new Thunk(tail, env)
        );
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("(%s : %s)", head, tail);
    }
}
