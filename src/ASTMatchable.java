
public abstract class ASTMatchable extends ASTNode {
    // anything that can represents a pattern that values can be matched against

    /**
     * try to bind the value subject to this pattern
     * @param subject the value to try and match to this pattern
     * @param env the environment to bind any variable names into
     * @throws PatternMatchFailedException if the value doesn't match the pattern
     */
    abstract void bindMatch(Value subject, Environment env) throws PatternMatchFailedException;
    // TODO: track new variables bound this match, detect any name bound more than once -> exception
    //  (not a pattern match exception!! --- this is a syntax error (invalid pattern))

}
