package fun.ast;

import fun.eval.Environment;
import fun.eval.EvaluationException;
import fun.eval.PatternMatchFailedException;
import fun.eval.Thunk;
import fun.types.Inferer;
import fun.types.Type;
import fun.types.TypeEnvironment;
import fun.types.TypeErrorException;
import fun.values.Value;

import java.util.List;
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
        throw new UnsupportedOperationException(); // TODO: implement
        // each case must unify with `subject`
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
