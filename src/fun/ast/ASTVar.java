package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.values.Value;

public class ASTVar extends ASTNode implements ASTMatchable {
    private final String name;

    public ASTVar(String name) {
        this.name = name;
    }

    @Override
    public Value evaluate(Environment env) throws EvaluationException { // TODO: specialise to name error or something
        return env.lookup(name).force();
    }

    @Override
    public void bindMatch(Thunk subject, Environment env) {
        env.bind(name, subject);
    }

    @Override
    public String toString() {
        return name;
    }
}
