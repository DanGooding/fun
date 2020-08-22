package fun.ast;

import fun.eval.Environment;
import fun.eval.Thunk;

public class ASTUnderscore implements ASTMatchable {

    public ASTUnderscore() {}

    @Override
    public void bindMatch(Thunk subject, Environment<Thunk> env) {
        // do nothing - match always succeeds, matched value discarded
        // if was lazy, wouldn't evaluate subject to a value
    }

    // TODO: ensure '_' is not a valid name
    @Override
    public String toString() {
        return "_";
    }
}
