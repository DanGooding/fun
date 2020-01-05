package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.values.Value;

// TODO: rename to something more helpful (expr?)
public abstract class ASTNode {

    /**
     * evaluate the tree from this node to yield a value
     * @param env a set of name bindings
     * @return the result of evaluation
     */
    public abstract Value evaluate(Environment env) throws EvaluationException;

    public Value evaluate() throws EvaluationException {
        return evaluate(new Environment());
    }
}
