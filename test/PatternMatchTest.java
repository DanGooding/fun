import junit.framework.TestCase;
import org.junit.Test;

import java.nio.channels.MulticastChannel;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PatternMatchTest {

    @Test
    public void matches_forEqualPrimitives() throws EvaluationException {
        // ARRANGE

        // let (1, False, 3, True) = (1, False, 3, True) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralBool(false),
                    new ASTLiteralInt(3),
                    new ASTLiteralBool(true)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralBool(false),
                    new ASTLiteralInt(3),
                    new ASTLiteralBool(true)
                )),
                new ASTLiteralInt(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(0);

    }

    @Test(expected = PatternMatchException.class)
    public void fails_forUnequalPrimitives() throws EvaluationException {
        // ARRANGE
        // let (1, 2) = (1, 3) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(2)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(3)
                )),
                new ASTLiteralInt(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test(expected = PatternMatchException.class)
    public void fails_forPatternLonger() throws EvaluationException {
        // ARRANGE
        // let (1, 2, 3) = (1, 2) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(2),
                    new ASTLiteralInt(3)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(2)
                )),
                new ASTLiteralInt(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test(expected = PatternMatchException.class)
    public void fails_forPatternShorter() throws EvaluationException {
        // ARRANGE
        // let (1, 2) = (1, 2, 3) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(2)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(2),
                    new ASTLiteralInt(3)
                )),
                new ASTLiteralInt(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void matches_emptyTuples() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTLet(
                ASTTuple.unit(),
                ASTTuple.unit(),
                new ASTLiteralInt(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(0);
    }

    @Test
    public void matches_withNestedTuples() throws EvaluationException {
        // ARRANGE

        // let (1, (2, (3, 4), 5), (6, 7)) = (1, (2, (3, 4), 5), (6, 7)) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(2),
                        new ASTTuple(List.of(
                            new ASTLiteralInt(3),
                            new ASTLiteralInt(4)
                        )),
                        new ASTLiteralInt(5)
                    )),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(6),
                        new ASTLiteralInt(7)
                    ))
                )),

                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(2),
                        new ASTTuple(List.of(
                            new ASTLiteralInt(3),
                            new ASTLiteralInt(4)
                        )),
                        new ASTLiteralInt(5)
                    )),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(6),
                        new ASTLiteralInt(7)
                    ))
                )),

                new ASTLiteralInt(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(0);

    }

    @Test
    public void assignment_worksCorrectly() throws EvaluationException {
        // ARRANGE
        // let (x, y) = (10, 2) in x - y
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTVar("x"),
                    new ASTVar("y")
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(10),
                    new ASTLiteralInt(2)
                )),
                new ASTMinus(
                    new ASTVar("x"),
                    new ASTVar("y")
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(8);

    }

    @Test  // TODO: give this a dedicated exception type + ensure it is caught at parse time
    public void fails_forNonMatchableTupleElement() throws EvaluationException {
        // ARRANGE
        // let (1, x + 2) = (1, 4) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTPlus(
                        new ASTVar("x"),
                        new ASTLiteralInt(2)
                    )
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInt(1),
                    new ASTLiteralInt(4)
                )),
                new ASTLiteralInt(0)
            );

        // ACT + ASSERT
        try {
            ast.evaluate();
            TestCase.fail("expected exception");
        } catch(EvaluationException e) {
            if (e instanceof PatternMatchException) {
                TestCase.fail("wrong exception type - invalid pattern, not a match failure");
            }
        }
    }

    @Test //(expected = EvaluationException.class) // TODO: make a dedicated exception subtype
    public void throwsSuitable_OnMultipleAssignmentsToSameVariable() throws EvaluationException {
        // ARRANGE

        // let ((1, x), (x, False)) = ((1, 3), (3, False)) in 0

        ASTNode ast =
            new ASTLet(
                new ASTTuple(List.of(
                    new ASTTuple(List.of(
                        new ASTLiteralInt(1),
                        new ASTVar("x")
                    )),
                    new ASTTuple(List.of(
                        new ASTVar("x"),
                        new ASTLiteralBool(false)
                    ))
                )),
                new ASTTuple(List.of(
                    new ASTTuple(List.of(
                        new ASTLiteralInt(1),
                        new ASTLiteralInt(3)
                    )),
                    new ASTTuple(List.of(
                        new ASTLiteralInt(3),
                        new ASTLiteralBool(false)
                    ))
                )),
                new ASTLiteralInt(0)
            );

        // ACT + ASSERT
        try {
            ast.evaluate();
            TestCase.fail("exception expected"); // expected an exception
        } catch(EvaluationException e) {
            if (e instanceof PatternMatchException) {
                // PatternMatchException indicates a correctly formed pattern that didn't match
                // actually have an incorrectly formed pattern
                TestCase.fail("wrong exception type - illegal operation, not match failure");
            }
        }

    }

    // TODO: add more extensive tests of these / else abstract the matching part
    //  to have one unified system for let, fun, case
    @Test
    public void works_inFunctionParameter() throws EvaluationException {
        // ARRANGE
        // (fun (a, b) -> a - b) (2, 3)
        ASTNode ast =
            new ASTApply(
                new ASTFun(
                    new ASTTuple(List.of(
                        new ASTVar("a"),
                        new ASTVar("b")
                    )),
                    new ASTMinus(
                        new ASTVar("a"),
                        new ASTVar("b")
                    )
                ),
                new ASTTuple(List.of(
                    new ASTLiteralInt(2),
                    new ASTLiteralInt(3)
                ))
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(-1);
    }



}
