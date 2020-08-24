package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.*;
import fun.values.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ASTCase extends ASTNode {
    // case <ast> of
    //     <pattern1> -> <ast1>
    //     <pattern2> -> <ast2>
    //     ...

    private final ASTNode subject;
    private final List<ASTCaseOption> options;

    public ASTCase(ASTNode subject, List<ASTCaseOption> options) {
        this.subject = subject;
        this.options = List.copyOf(options);
    }

    @Override
    public Value evaluate(Environment<Thunk> env) throws EvaluationException {
        Thunk subjectThunk = new Thunk(subject, env);

        for (ASTCaseOption option : options) {

            try {
                Environment<Thunk> innerEnv = new Environment<>(env); // (cannot reuse)

                option.pattern.bindMatch(subjectThunk, innerEnv);

                return option.body.evaluate(innerEnv);

            } catch (PatternMatchFailedException e) {
                continue;
            }

        }

        throw new EvaluationException("Non-exhaustive patterns in case");

    }

    @Override
    public Type inferType(Inferer inferer, TypeEnvironment env) throws TypeErrorException {

        // each case must unify with `subject`
        Type subjectType = subject.inferType(inferer, env);
        Type resultType = inferer.freshVariable();

        for (ASTCaseOption option : options) {
            // TODO: this is identical to ASTLambda's
            Map<String, Type> newBindings = new HashMap<>();
            Type patternType = option.pattern.inferPatternType(inferer, newBindings);

            TypeEnvironment bodyEnv = new TypeEnvironment(env);
            for (String boundName : newBindings.keySet()) {
                bodyEnv.bind(boundName, newBindings.get(boundName));
            }
            Type bodyType = option.body.inferType(inferer, bodyEnv);

            inferer.unify(subjectType, patternType);
            inferer.unify(bodyType, resultType);
        }
        return resultType;
    }

    @Override
    public String toString() {
        String caseStrings = options
            .stream()
            .map(ASTCaseOption::toString)
            .collect(Collectors.joining(" | ", "", ""));
        return String.format("(case %s of %s)", subject, caseStrings);
    }
}
