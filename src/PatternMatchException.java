/**
 * thrown when a pattern match fails,
 * it may be caught by the evaluator if the match was refutable
 * or not in the case of irrefutable pattern matches (only one case / final case)
 */
public class PatternMatchException extends EvaluationException {
    // TODO: is this logically the same as other evaluation exceptions

    PatternMatchException(String message) {
        super(message);
    }
}
