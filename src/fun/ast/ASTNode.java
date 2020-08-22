package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.types.TypeErrorException;
import fun.values.Value;

// TODO: rename to something more helpful (expr?)
public abstract class ASTNode {

    /**
     * evaluate the tree from this node to yield a value
     * @param env a set of name bindings
     * @return the result of evaluation
     */
    public abstract Value evaluate(Environment<Thunk> env) throws EvaluationException;

    public Value evaluate() throws EvaluationException {
        return evaluate(new Environment<>());
    }

    public abstract Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException;

}
