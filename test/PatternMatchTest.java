import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PatternMatchTest {

    @Test
    public void matches_forEqualPrimitives() throws EvaluationException {
        // ARRANGE

        // let (1, False, 3, True) = (1, False, 3, True) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralBool(false),
                    new ASTLiteralInteger(3),
                    new ASTLiteralBool(true)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralBool(false),
                    new ASTLiteralInteger(3),
                    new ASTLiteralBool(true)
                )),
                new ASTLiteralInteger(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(0);

    }

    @Test(expected = EvaluationException.class)
    public void fails_forUnequalPrimitives() throws EvaluationException {
        // ARRANGE
        // let (1, 2) = (1, 3) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(2)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(3)
                )),
                new ASTLiteralInteger(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test(expected = EvaluationException.class)
    public void fails_forPatternLonger() throws EvaluationException {
        // ARRANGE
        // let (1, 2, 3) = (1, 2) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(2),
                    new ASTLiteralInteger(3)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(2)
                )),
                new ASTLiteralInteger(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test(expected = EvaluationException.class)
    public void fails_forPatternShorter() throws EvaluationException {
        // ARRANGE
        // let (1, 2) = (1, 2, 3) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(2)
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new ASTLiteralInteger(2),
                    new ASTLiteralInteger(3)
                )),
                new ASTLiteralInteger(0)
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void matches_emptyTuples() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of()),
                new ASTTuple(List.of()),
                new ASTLiteralInteger(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(0);
    }

    @Test
    public void matches_withNestedTuples() throws EvaluationException {
        // ARRANGE

        // let (1, (2, (3, 4), 5), (6, 7)) = (1, (2, (3, 4), 5), (6, 7)) in 0
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTLiteralInteger(1),
                    new ASTTuplePattern(List.of(
                        new ASTLiteralInteger(2),
                        new ASTTuplePattern(List.of(
                            new ASTLiteralInteger(3),
                            new ASTLiteralInteger(4)
                        )),
                        new ASTLiteralInteger(5)
                    )),
                    new ASTTuplePattern(List.of(
                        new ASTLiteralInteger(6),
                        new ASTLiteralInteger(7)
                    ))
                )),

                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(2),
                        new ASTTuple(List.of(
                            new ASTLiteralInteger(3),
                            new ASTLiteralInteger(4)
                        )),
                        new ASTLiteralInteger(5)
                    )),
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(6),
                        new ASTLiteralInteger(7)
                    ))
                )),

                new ASTLiteralInteger(0)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(0);

    }

    @Test
    public void assignment_worksCorrectly() throws EvaluationException {
        // ARRANGE
        // let (x, y) = (10, 2) in x - y
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTVar("x"),
                    new ASTVar("y")
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(10),
                    new ASTLiteralInteger(2)
                )),
                new ASTMinus(
                    new ASTVar("x"),
                    new ASTVar("y")
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(8);

    }

    @Test(expected = EvaluationException.class) // TODO: make a dedicated exception subtype
    public void throwsSuitable_OnMultipleAssignmentsToSameVariable() throws EvaluationException {
        // ARRANGE

        // let ((1, x), (x, False)) = ((1, 3), (3, False)) in 0

        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTTuplePattern(List.of(
                        new ASTLiteralInteger(1),
                        new ASTVar("x")
                    )),
                    new ASTTuplePattern(List.of(
                        new ASTVar("x"),
                        new ASTLiteralBool(false)
                    ))
                )),
                new ASTTuple(List.of(
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(1),
                        new ASTLiteralInteger(3)
                    )),
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(3),
                        new ASTLiteralBool(false)
                    ))
                )),
                new ASTLiteralInteger(0)
            );

        // ACT + ASSERT
        ast.evaluate();
    }

    // TODO: add more extensive tests of these / else abstract the matching part
    //  to have one unified system for let, fun, case
    @Test
    public void works_inFunctionParameter() throws EvaluationException {
        // ARRANGE
        // (fun (a, b) -> a - b) (2, 3)
        ASTNode ast =
            new ASTApply(
                new ASTLambda(
                    new ASTTuplePattern(List.of(
                        new ASTVar("a"),
                        new ASTVar("b")
                    )),
                    new ASTMinus(
                        new ASTVar("a"),
                        new ASTVar("b")
                    )
                ),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(2),
                    new ASTLiteralInteger(3)
                ))
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(-1);
    }




}
