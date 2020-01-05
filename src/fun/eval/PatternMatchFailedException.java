package fun.eval;

/**
 * thrown by fun.ast.ASTMatchable when an attempted pattern match fails
 * must be caught, and either another pattern tried (case statement)
 * or an fun.eval.EvaluationException thrown (let / fun statement) if the match
 * was irrefutable
 */
public class PatternMatchFailedException extends Exception {

    public PatternMatchFailedException(String message) {
        super(message);
    }
}
