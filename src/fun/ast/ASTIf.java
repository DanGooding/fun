package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.RuntimeTypeErrorException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.BoolValue;
import fun.values.Value;

public class ASTIf extends ASTNode {

    private final ASTNode condition;
    private final ASTNode trueBranch;
    private final ASTNode falseBranch;

    public ASTIf(ASTNode condition, ASTNode trueBranch, ASTNode falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    @Override
    public Value evaluate(Environment<Thunk> env) throws EvaluationException {
        Value conditionValue = condition.evaluate(env);

        if (conditionValue instanceof BoolValue) {
            if (((BoolValue) conditionValue).getValue()) {
                return trueBranch.evaluate(env);
            }else {
                return falseBranch.evaluate(env);
            }
        }else {
            throw new RuntimeTypeErrorException("if condition must have type bool");
        }
    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {
        Type conditionType = condition.inferType(inferer, env);
        Type trueBranchType = trueBranch.inferType(inferer, env);
        Type falseBranchType = falseBranch.inferType(inferer, env);

        inferer.unify(conditionType, new TypeBool());
        inferer.unify(trueBranchType, falseBranchType);

        return trueBranchType;
    }

    @Override
    public String toString() {
        return String.format("(if %s then %s else %s)", condition, trueBranch, falseBranch);
    }
}
