package fun.ast;

import fun.eval.Environment;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;

import java.util.Map;

public class ASTUnderscore implements ASTMatchable {

    public ASTUnderscore() {}

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) {
        // do nothing - match always succeeds, matched value discarded
    }

    @Override
    public Type inferPatternType(Inferer inferer, Map<String, Type> bindings) {
        return inferer.freshVariable();
    }

    // TODO: ensure '_' is not a valid name
    @Override
    public String toString() {
        return "_";
    }
}
