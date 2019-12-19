public abstract class ASTNode {

    /**
     * evaluate the tree from this node to yield a value
     * @param env a set of name bindings
     * @return the result of evaluation
     */
    abstract Value evaluate(Environment env);

    public Value evaluate() {
        return evaluate(new Environment());
    }
}
