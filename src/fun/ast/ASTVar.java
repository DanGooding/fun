package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.Value;

import java.util.Map;

public class ASTVar extends ASTNode implements ASTMatchable {
    private final String name;

    public ASTVar(String name) {
        this.name = name;
    }

    @Override
    public Value evaluate(Environment<Thunk> env) throws EvaluationException { // TODO: specialise to name error or something
        Thunk t = env.lookup(name);
        if (t == null) {
            throw new EvaluationException(String.format("unbound variable %s", name));
        }
        return env.lookup(name).force();
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {
        if (!env.hasName(name)) {
            throw new TypeErrorException(String.format("unbound variable %s", name));
        }
        return env.lookup(name).instantiate(inferer);
    }

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) {
        env.bind(name, subject);
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) throws TypeErrorException {
        if (bindings.containsKey(name)) {
            // cannot bind the same name twice in a pattern
            throw new TypeErrorException(String.format("variable %s bound twice in pattern", name));
        }
        TypeVariable typeVariable = inferer.freshVariable();
        bindings.put(name, typeVariable);
        return typeVariable;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
