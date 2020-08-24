package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeErrorException;

import java.util.Map;

/**
 * anything that can represent a pattern which values can be matched against
 */
public interface ASTMatchable {

    /**
     * try to bind the value subject to this pattern
     * @param subject thunk of the value to bind to this pattern
     * @param env the environment to bind any variable names into
     * @throws PatternMatchFailedException if the value doesn't match the pattern
     */
    void bindMatch(Thunk subject, Environment<Thunk> env) throws PatternMatchFailedException, EvaluationException;
    // TODO: allow binding names to sub patterns (@ in Haskell, `as` in OCaml)

    /**
     * infer the type of an expression that would match this pattern,
     * recording the types of the introduced variables
     */
    Type inferPatternType(Inferer inferer, Map<String, Type> bindings) throws TypeErrorException;



    // TODO: don't pass down & modify an fun.eval.Environment, use a Map<String, fun.values.Value>  -- is this worse
    //       and then once all bindings done, create a new environment
    //       giving proper immutability to environments
    //   one worker function to recurse with Map<String, fun.values.Value>,
    //   one wrapper function to create a new fun.eval.Environment with those bindings
    //   maybe a candidateBindings type ? - that detects duplicate bindings & throws an error
}
