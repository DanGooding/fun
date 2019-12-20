public class ASTUnderscore extends ASTMatchable {

    ASTUnderscore() {}

    @Override
    Value evaluate(Environment env) throws EvaluationException {
        // TODO: this cannot be evaluated, is Matchable really a subtype of ASTNode ?
        // TODO: throw a more specific exception - will this be prevented by the parser anyway?
        throw new EvaluationException("cannot evaluate");
    }

    @Override
    void bindMatch(Value subject, Environment env) throws PatternMatchException {
        // do nothing - match always succeeds, matched value discarded
        // if was lazy, wouldn't evaluate subject to a value
    }

    @Override
    public String toString() {
        return "_";
    }
}
