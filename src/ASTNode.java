public abstract class ASTNode {

    /**
     * evaluate the tree from this node to yield a value
     * @param env a set of name bindings
     * @return a value
     */
    abstract ConstantValue evaluate(Environment env);

    public ConstantValue evaluate() {
        return evaluate(new Environment());
    }
}
