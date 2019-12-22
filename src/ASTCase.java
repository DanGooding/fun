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
    Value evaluate(Environment env) throws EvaluationException {
        Value subjectValue = subject.evaluate(env);

        for (ASTCaseOption option : options) {

            try {
                Environment innerEnv = new Environment(env);

                option.pattern.bindMatch(subjectValue, innerEnv);

                return option.body.evaluate(innerEnv);

            } catch (PatternMatchFailedException e) {
                continue;
            }

        }

        throw new EvaluationException("Non-exhaustive patterns in case");

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
