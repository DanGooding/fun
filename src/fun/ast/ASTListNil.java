package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.types.TypeErrorException;
import fun.values.ListNilValue;
import fun.values.Value;

public class ASTListNil extends ASTNode {
    @Override
    public Value evaluate(Environment<Thunk> env) {
        return new ListNilValue();
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) {
        throw new UnsupportedOperationException();
    }
}
