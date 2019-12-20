import junit.framework.TestCase;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class IfTest {

    @Test
    public void if_takesTrueBranch_forTrue() throws EvaluationException {
        // ARRANGE
        ASTNode ast = new ASTIf(
            new ASTLiteralBool(true),
            new ASTLiteralInt(1),
            new ASTLiteralInt(0)
        );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(1);
    }

    @Test
    public void if_takesFalseBranch_forFalse() throws EvaluationException {
        // ARRANGE
        ASTNode ast = new ASTIf(
            new ASTLiteralBool(false),
            new ASTLiteralInt(1),
            new ASTLiteralInt(0)
        );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntValue.class);
        assertThat(((IntValue) result).getValue()).isEqualTo(0);
    }

    @Test
    public void if_doesntEvaluateFalseBranch_forTrue() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTIf(
                new ASTLiteralBool(true),
                new ASTLiteralInt(1),
                new ASTLiteralInt(0) {
                    @Override
                    IntValue evaluate(Environment env) {
                        TestCase.fail();  // this should not be evaluated, fail the test if it is
                        return super.evaluate(env);
                    }
                }
            );

        // ACT
        Value result = ast.evaluate();
    }

    @Test
    public void if_doesntEvaluateTrueBranch_forFalse() throws EvaluationException {
        // ARRANGE
        ASTNode ast =
            new ASTIf(
                new ASTLiteralBool(false),
                new ASTLiteralInt(1) {
                    @Override
                    IntValue evaluate(Environment env) {
                        TestCase.fail();  // this should not be evaluated, fail the test if it is
                        return super.evaluate(env);
                    }
                },
                new ASTLiteralInt(0)
            );

        // ACT
        Value result = ast.evaluate();
    }


}
