package fun.eval;

import fun.ast.*;
import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.values.IntegerValue;
import fun.values.Value;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class LazinessTest {

    // TODO: these don't need ot be static do they?
    static class FailOnEval extends ASTNode {
        @Override
        public Value evaluate(Environment<Thunk> env) {
            TestCase.fail("shouldn't evaluate this");
            return null;
        }

        @Override
        public Type inferType(Inferer inferer, TypeEnvironment env) {
            throw new UnsupportedOperationException();
        }
    }

    static class EvalCounter extends ASTNode {

        private int evalCount;
        private final ASTNode inner;

        EvalCounter(ASTNode inner) {
            this.inner = inner;
            evalCount = 0;
        }

        @Override
        public Value evaluate(Environment<Thunk> env) throws EvaluationException {
            evalCount += 1;
            return inner.evaluate(env);
        }

        @Override
        public Type inferType(Inferer inferer, TypeEnvironment env) {
            throw new UnsupportedOperationException();
        }

        int getEvalCount() {
            return evalCount;
        }
    }


    @Test
    public void let_doesntEvaluateAssigned_whenVarNotReferenced() throws EvaluationException {
        // ARRANGE
        // let x = <fail> in 0
        ASTNode ast =
            new ASTLet(
                new ASTVar("x"),
                new FailOnEval(),
                new ASTLiteralInteger(0)
            );

        // ACT
        ast.evaluate();

    }

    @Test
    public void lambda_doesntEvaluateAssigned_whenParameterNotReferenced() throws EvaluationException {
        // ARRANGE
        // (lambda _ -> 0) (<fail>)
        ASTNode ast =
            new ASTApply(
                new ASTLambda(
                    new ASTUnderscore(),
                    new ASTLiteralInteger(0)
                ),
                new FailOnEval()
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void onlyEvaluatesOnce_whenVarRepeated() throws EvaluationException {
        // ARRANGE
        EvalCounter evalCounter_literal1 = new EvalCounter(new ASTLiteralInteger(1));
        // let x = 1 in x + x
        ASTNode ast =
            new ASTLet(
                new ASTVar("x"),
                evalCounter_literal1,
                new ASTPlus(
                    new ASTVar("x"),
                    new ASTVar("x")
                )
            );

        // ACT
        ast.evaluate();

        // ASSERT
        assertThat(evalCounter_literal1.getEvalCount()).isEqualTo(1);
    }

    @Test
    public void tupleElements_areLazy() throws EvaluationException {
        // ARRANGE
        // (<fail>, <fail>)
        ASTNode ast =
            new ASTTuple(List.of(
                new FailOnEval(),
                new FailOnEval()
            ));

        // ACT
        ast.evaluate();
    }

    @Test
    public void patternMatch_doesntEvaluate_wholeTuple() throws EvaluationException {
        // ARRANGE
        // let (x, y) = (1, <fail>) in x
        ASTNode ast =
            new ASTLet(
                new ASTTuplePattern(List.of(
                    new ASTVar("x"),
                    new ASTVar("y")
                )),
                new ASTTuple(List.of(
                    new ASTLiteralInteger(1),
                    new FailOnEval()
                )),
                new ASTVar("x")
            );

        // ACT
        ast.evaluate();
    }

    @Test
    public void case_EvaluatesSubjectOnce() throws EvaluationException {
        // ARRANGE
        EvalCounter evalCounter_literal2 = new EvalCounter(new ASTLiteralInteger(2));
        // case (2, <fail>) of
        //     (1, _) -> 1
        //     (2, x) -> 2
        ASTNode ast =
            new ASTCase(
                new ASTTuple(List.of(
                    evalCounter_literal2,
                    new FailOnEval()
                )),
                List.of(
                    new ASTCaseOption(
                        new ASTTuplePattern(List.of(
                            new ASTLiteralInteger(1),
                            new ASTUnderscore()
                        )),
                        new ASTLiteralInteger(1)
                    ),
                    new ASTCaseOption(
                        new ASTTuplePattern(List.of(
                            new ASTLiteralInteger(2),
                            new ASTVar("x")
                        )),
                        new ASTLiteralInteger(2)
                    )
                )
            );

        // ACT
        Value result = ast.evaluate();

        // ASSERT
        assertThat(result).isInstanceOf(IntegerValue.class);
        assertThat(((IntegerValue) result).getValue().intValueExact()).isEqualTo(2);

        assertThat(evalCounter_literal2.getEvalCount()).isEqualTo(1);
    }

    // TODO:
    //  let (x, y) = f 100 in 0
    //  should f 100 be evaluated at all ?
    //  with type checking - no
    //  without, must evaluate to be sure its result is a 2-tuple




    // TODO: @Jailbreak to test private methods
}
