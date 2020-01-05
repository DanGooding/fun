package fun.ast;

public class ASTCaseOption {

    // TODO: make private and add getters ?
    final ASTMatchable pattern;
    final ASTNode body;

    public ASTCaseOption(ASTMatchable pattern, ASTNode body) {
        this.pattern = pattern;
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", pattern, body);
    }
}
