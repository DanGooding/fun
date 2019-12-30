public class Parser {

    private final TokenStream tokenStream;
    private Token currentToken;

    Parser(String input) {
        tokenStream = new TokenStream(input);
        advance();
    }

    private void advance() {
        currentToken = tokenStream.nextToken();
    }

    /**
     * if currentToken is of given type, advance() to next token
     * otherwise raise an exception
     */
    private void eat(TokenType type) {
        if (currentToken.type == type) {
            advance();
        }else {
            // TODO: proper exception type (checked?)
            throw new RuntimeException(String.format("parse error, %s expected", type));
        }
    }

    // TODO: top level: multiple expressions - this requires changes to the AST too

    // TODO: make private, except
    ASTNode parseExpr() {

        switch (currentToken.type) {
            case INTEGER:
                return parseInt();

            case NAME:
                return parseName();

            case OPEN_BRACKET: {
                // TODO: may be a tuple
                eat(TokenType.OPEN_BRACKET);
                ASTNode result = parseExpr();
                eat(TokenType.CLOSE_BRACKET);
                return result;
            }

            case LET: {
                eat(TokenType.LET);
                ASTMatchable pattern = parsePattern();
                eat(TokenType.ASSIGN_EQUALS);
                ASTNode assigned = parseExpr();
                eat(TokenType.IN);
                ASTNode body = parseExpr();
                return new ASTLet(pattern, assigned, body);
            }

            case IF: {
                eat(TokenType.IF);
                ASTNode condition = parseExpr();
                eat(TokenType.THEN);
                ASTNode thenClause = parseExpr();
                eat(TokenType.ELSE);
                ASTNode elseClause = parseExpr();
                return new ASTIf(condition, thenClause, elseClause);
            }

            case LAMBDA: {
                eat(TokenType.LAMBDA);
                ASTMatchable param = parsePattern();
                eat(TokenType.ARROW);
                ASTNode body = parseExpr();
                return new ASTLambda(param, body);
            }

            default:
                throw new RuntimeException("parse error in expression");
        }
    }

    private ASTMatchable parsePattern() {
        if (currentToken.type == TokenType.NAME) {
            return parseName();
        }else if (currentToken.type == TokenType.UNDERSCORE) {
            advance();
            return new ASTUnderscore();
        }else if (currentToken.type == TokenType.INTEGER) {
            return parseInt();
        }
        // TODO: allow tuples

        throw new RuntimeException("parse error in pattern");
    }

    private ASTLiteralInt parseInt() {
        // TODO: should this check the token type? (like other parse_ functions)
        ASTLiteralInt result = new ASTLiteralInt(Integer.parseInt(currentToken.string));
        advance();
        return result;
    }

    private ASTVar parseName() {
        ASTVar result = new ASTVar(currentToken.string);
        advance();
        return result;
    }


}
