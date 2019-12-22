/**
 * thrown by ASTMatchable when an attempted pattern match fails
 * must be caught, and either another pattern tried (case statement)
 * or an EvaluationException thrown (let / fun statement) if the match
 * was irrefutable
 */
public class PatternMatchFailedException extends Exception {

    PatternMatchFailedException(String message) {
        super(message);
    }
}
