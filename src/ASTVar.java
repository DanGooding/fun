class ASTVar extends ASTNode implements ASTMatchable {
    private final String name;

    ASTVar(String name) {
        this.name = name;
    }

    @Override
    Value evaluate(Environment env) throws EvaluationException { // TODO: specialise to name error or something
        return env.lookup(name).force();
    }

    @Override
    public void bindMatch(Thunk subject, Environment env) {
        env.bind(name, subject);
    }

    @Override
    public String toString() {
        return name;
    }
}
