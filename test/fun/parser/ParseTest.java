package fun.parser;

import fun.ast.ASTNode;
import fun.eval.EvaluationException;
import fun.parser.Parser;
import fun.values.BoolValue;
import fun.values.IntegerValue;
import fun.values.Value;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ParseTest {

    @Test
    public void worksCorrectly_forLeftAssoc() throws EvaluationException {
        // ARRANGE
        String input = "1 + 2 * 3 - 4 * 5";
        Parser p = new Parser(input);

        // ACT
        ASTNode ast = p.parseExpr();
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(-13);
    }

    @Test
    public void worksCorrectly_forRightAssoc() throws EvaluationException {
        // ARRANGE
        String input = "4 ^ 3 ^ 2 ^ 1";
        Parser p = new Parser(input);

        // ACT
        ASTNode ast = p.parseExpr();
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(262144);
    }

    @Test
    public void worksCorrectly_forAssocMixture() throws EvaluationException {
        // ARRANGE
        String input = "1 + 2 - 3 * 4 * 5 ^ 2 * 3 - 4 ^ 2 ^ 0 - 1 + 1000 == 10 * 9 + 2 ^ 3";
        Parser p = new Parser(input);

        // ACT
        ASTNode ast = p.parseExpr();
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(BoolValue.class);
        assertThat(((BoolValue) result).getValue()).isEqualTo(true);
    }

    @Test(expected = RuntimeException.class)
    public void throws_forMultipleNonAssociative() throws EvaluationException {
        // ARRANGE
        String input = "1 == 2 == 3";
        Parser p = new Parser(input);

        // ACT
        ASTNode ast = p.parseExpr();
        Value result = ast.evaluate();

    }

    // TODO: tuples, function application (inc multiple args)

}
