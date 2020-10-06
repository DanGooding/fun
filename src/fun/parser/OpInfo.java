package fun.parser;

import fun.ast.ASTNode;

import java.util.function.BiFunction;

class OpInfo {
    private final int precedence;
    private final Assoc associativity;
    private final BiFunction<ASTNode, ASTNode, ASTNode> astConstructor;

    OpInfo(int precedence, Assoc associativity, BiFunction<ASTNode, ASTNode, ASTNode> astConstructor) {
        this.precedence = precedence;
        this.associativity = associativity;
        this.astConstructor = astConstructor;
    }

    public int getPrecedence() {
        return precedence;
    }

    public Assoc getAssociativity() {
        return associativity;
    }

    public BiFunction<ASTNode, ASTNode, ASTNode> getAstConstructor() {
        return astConstructor;
    }

    @Override
    public String toString() {
        return String.format("precedence %d, associativity %s", precedence, associativity);
    }
}
