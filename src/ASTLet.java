class ASTLet extends ASTNode {
    // let <matchable> = <ast> in <ast>

    private final ASTMatchable pattern;
    private final ASTNode assigned;
    private final ASTNode body;

    ASTLet(ASTMatchable pattern, ASTNode assigned, ASTNode body) {
        this.pattern = pattern;
        this.assigned = assigned;
        this.body = body;
    }

    ASTLet(String name, ASTNode assigned, ASTNode body) {
        this(new ASTVar(name), assigned, body);
    }

    @Override
    Value evaluate(Environment env) throws EvaluationException {
        Environment bodyEnv = new Environment(env);

        try {
            pattern.bindMatch(new Thunk(assigned, env), bodyEnv); // TODO: pass bodyEnv to allow recursion (?)

        } catch (PatternMatchFailedException e) {
            throw new EvaluationException(e.getMessage());
        }

        return body.evaluate(bodyEnv);
    }

    @Override
    public String toString() {
        return String.format("(let %s = %s in %s)", pattern, assigned, body);
    }
}
