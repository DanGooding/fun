package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.RuntimeTypeErrorException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.*;

// TODO: add not equals ? or just use boolean not along with this?
public class ASTEquals extends ASTNode {

    private final ASTNode left;
    private final ASTNode right;

    public ASTEquals(ASTNode left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    private boolean valuesEqual(Value leftValue, Value rightValue) throws EvaluationException {
        if (leftValue instanceof ConstantValue && rightValue instanceof ConstantValue) {
            // TODO: atrocious, have a method on fun.values.ConstantValue or something
            // type tag + lookup table of equatable types + method

            if (leftValue instanceof BoolValue && rightValue instanceof BoolValue) {
                return ((BoolValue) leftValue).getValue() == ((BoolValue) rightValue).getValue();

            } else if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {
                return ((IntegerValue) leftValue).getValue().equals(((IntegerValue) rightValue).getValue());

            } else if (leftValue instanceof TupleValue leftTuple && rightValue instanceof TupleValue rightTuple) {

                if (leftTuple.size() != rightTuple.size()) return false;

                for (int i = 0; i < leftTuple.size(); i++) {
                    Value leftElement = leftTuple.getElement(i).force();
                    Value rightElement = rightTuple.getElement(i).force();
                    if (!this.valuesEqual(leftElement, rightElement)) return false;
                }

                return true;

            } else if (leftValue.getClass() == rightValue.getClass()) {
                throw new UnsupportedOperationException(
                        String.format("equality on %s not implemented", leftValue.getClass().getSimpleName()));

            } else {
                throw new RuntimeTypeErrorException("cannot equate values of different type");
            }

        } else {
            throw new RuntimeTypeErrorException("functions are not equatable");
        }
    }

    @Override
    public BoolValue evaluate(Environment<Thunk> env) throws EvaluationException {
        Value leftValue = left.evaluate(env);
        Value rightValue = right.evaluate(env);

        return new BoolValue(this.valuesEqual(leftValue, rightValue));
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {
        // TODO: implement proper equality types (ML style easier than typeclasses)

        Type leftType = left.inferType(inferer, env);
        Type rightType = right.inferType(inferer, env);

        inferer.unify(leftType, rightType);

        // TODO: cannot equate functions

        return new TypeBool();
    }

    @Override
    public String toString() {
        return String.format("(%s == %s)", left, right);
    }
}
