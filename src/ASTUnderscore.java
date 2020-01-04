public class ASTUnderscore implements ASTMatchable {

    ASTUnderscore() {}

    @Override
    public void bindMatch(Value subject, Environment env) {
        // do nothing - match always succeeds, matched value discarded
        // if was lazy, wouldn't evaluate subject to a value
    }

    // TODO: ensure '_' is not a valid name
    @Override
    public String toString() {
        return "_";
    }
}
