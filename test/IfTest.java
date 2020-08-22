import fun.ast.ASTIf;
import fun.ast.ASTLiteralBool;
import fun.ast.ASTLiteralInteger;
import fun.ast.ASTNode;
import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.values.IntegerValue;
import fun.values.Value;
import junit.framework.TestCase;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class IfTest {

    @Test
    public void if_takesTrueBranch_forTrue() throws EvaluationException {
        // ARRANGE
        ASTNode ast = new ASTIf(
            new ASTLiteralBool(true),
            new ASTLiteralInteger(1),
            new ASTLiteralInteger(0)
        );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(1);
    }

    @Test
    public void if_takesFalseBranch_forFalse() throws EvaluationException {
        // ARRANGE
        ASTNode ast = new ASTIf(
            new ASTLiteralBool(false),
            new ASTLiteralInteger(1),
            new ASTLiteralInteger(0)
        );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(0);
    }

    @Test
    public void if_doesntEvaluateFalseBranch_forTrue() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTIf(
                new ASTLiteralBool(true),
                new ASTLiteralInteger(1),
                new ASTLiteralInteger(0) {
                    @Override
                    public IntegerValue evaluate(Environment<Thunk> env) {
                        TestCase.fail();  // this should not be evaluated, fail the test if it is
                        return super.evaluate(env);
                    }
                }
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void if_doesntEvaluateTrueBranch_forFalse() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTIf(
                new ASTLiteralBool(false),
                new ASTLiteralInteger(1) {
                    @Override
                    public IntegerValue evaluate(Environment<Thunk> env) {
                        TestCase.fail();  // this should not be evaluated, fail the test if it is
                        return super.evaluate(env);
                    }
                },
                new ASTLiteralInteger(0)
            );

        // ACT
        ast.evaluate();
    }


}
