
public interface ASTMatchable {
    // anything that can represents a pattern that values can be matched against

    // TODO: a failed match is not exceptional -> is throwing an exception a bad way of communicating this ?

    /**
     * try to bind the value subject to this pattern
     * @param subject thunk of the value to bind to this pattern
     * @param env the environment to bind any variable names into
     * @throws PatternMatchFailedException if the value doesn't match the pattern
     */
    void bindMatch(Thunk subject, Environment env) throws PatternMatchFailedException, EvaluationException;
    // TODO: track new variables bound this match, detect any name bound more than once -> exception
    //  (not a pattern match exception!! --- this is a syntax error (invalid pattern))

    // TODO: allow binding names to sub patterns (@ in Haskell, `as` in OCaml)


    // TODO: don't pass down & modify an Environment, use a Map<String, Value>  -- is this worse
    //       and then once all bindings done, create a new environment
    //       giving proper immutability to environments
    //   one worker function to recurse with Map<String, Value>,
    //   one wrapper function to create a new Environment with those bindings
    //   maybe a candidateBindings type ? - that detects duplicate bindings & throws an error
}
