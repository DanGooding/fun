class ASTVar extends ASTNode implements ASTMatchable {
    private final String name;

    ASTVar(String name) {
        this.name = name;
    }

    @Override
    Value evaluate(Environment env) throws EvaluationException {
        if (env.hasName(name)) {
            return env.lookup(name);
        }
        // TODO: specialise NameError or something?
        throw new EvaluationException(String.format("unbound variable '%s'", name));
    }

    @Override
    public void bindMatch(Value subject, Environment env) {
        env.bind(name, subject);
    }

    @Override
    public String toString() {
        return name;
    }
}
