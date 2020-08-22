import fun.ast.*;
import fun.eval.EvaluationException;
import fun.eval.RuntimeTypeErrorException;
import fun.values.BoolValue;
import fun.values.Value;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class EqualsTest {

    @Test
    public void equals_givesTrue_forEqualInts() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTEquals(
                new ASTLiteralInteger(1),
                new ASTLiteralInteger(1)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(BoolValue.class);
        assertThat(((BoolValue) result).getValue()).isTrue();
    }

    @Test
    public void equals_givesFalse_forDifferentInts() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTEquals(
                new ASTLiteralInteger(1),
                new ASTLiteralInteger(2)
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(BoolValue.class);
        assertThat(((BoolValue) result).getValue()).isFalse();
    }

    @Test(expected = RuntimeTypeErrorException.class)
    public void equals_givesTypeError_forIntAndBool() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTEquals(
                new ASTLiteralInteger(3),
                new ASTLiteralBool(true)
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void equals_worksCorrectly_forTuples() throws EvaluationException {
        // ARRANGE
        // (10, False, (), (3, True)) == (10, False, (), (3, True))
        ASTNode ast =
            new ASTEquals(
                new ASTTuple(List.of(
                    new ASTLiteralInteger(10),
                    new ASTLiteralBool(false),
                    new ASTTuple(List.of()),
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(3),
                        new ASTLiteralBool(true)
                    ))
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(10),
                    new ASTLiteralBool(false),
                    new ASTTuple(List.of()),
                    new ASTTuple(List.of(
                        new ASTLiteralInteger(3),
                        new ASTLiteralBool(true)
                    ))
                ))
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(BoolValue.class);
        assertThat(((BoolValue) result).getValue()).isTrue();
    }


}
