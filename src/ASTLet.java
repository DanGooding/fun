class ASTLet extends ASTNode {
    // let name = <ast> in <ast>

    private final String name;
    private final ASTNode assigned;
    private final ASTNode body;

    ASTLet(String name, ASTNode assigned, ASTNode body) {
        this.name = name;
        this.assigned = assigned;
        this.body = body;
    }

    @Override
    Value evaluate(Environment env) {
        Value assignedValue = assigned.evaluate(env);

        Environment bodyEnv = new Environment(env);
        bodyEnv.bind(name, assignedValue);

        return body.evaluate(bodyEnv);
    }

    @Override
    public String toString() {
        return String.format("(let %s = %s in %s)", name, assigned, body);
    }
}
