import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final TokenStream tokenStream;
    private Token currentToken;

    Parser(String input) {
        tokenStream = new TokenStream(input);
        advance();
    }

    /**
     * move one token forward in the stream
     */
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
            // the expected token is possibly not the most useful thing, eg:
            // let t = (1, 2 in t
            // gives 'expected COMMA', rather than close bracket
            throw new RuntimeException(String.format("parse error, %s expected", type));
        }
    }

    // TODO: top level: multiple expressions - this requires changes to the AST too

    // TODO: make private, all except a root parseProgram function
    ASTNode parseExpr() {

        switch (currentToken.type) {

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

            default: {
                if (currentBeginsBaseExpr()) {
                    return parseApplication();
                }else {
                    throw new RuntimeException("parse error in expression");
                }
            }
        }
    }

    private ASTNode parseApplication() {
        // a base expression, applied to zero or more base expressions

        // app = app baseExpr
        //     | baseExpr

        ASTNode first = parseBaseExpr();  // TODO: custom here to avoid literals ?

        List<ASTNode> args = new ArrayList<>();
        while (currentBeginsBaseExpr()) {
            args.add(parseBaseExpr());
        }

        ASTNode app = first;
        for (ASTNode arg : args) {
            app = new ASTApply(app, arg);
        }

        return app;
    }

    private boolean currentBeginsBaseExpr() {
        switch (currentToken.type) {
            case INTEGER:
            case NAME:
            case OPEN_BRACKET:
                return true;
            default:
                return false;
        }
    }

    private ASTNode parseBaseExpr() {
        // a base expression is anything that can be an argument to a function or operation
        // without needing extra brackets for precedence to work

        switch (currentToken.type) {
            case INTEGER:
                return parseInt();

            case NAME:
                return parseName();

            case OPEN_BRACKET:
                return parseBracketedExpr();

            default:
                throw new RuntimeException("parse error in (base) expression");
        }

    }

    private ASTNode parseBracketedExpr() {
        eat(TokenType.OPEN_BRACKET);

        List<ASTNode> exprs = new ArrayList<>();
        while (currentToken.type != TokenType.CLOSE_BRACKET) {
            if (exprs.size() > 0) {
                eat(TokenType.COMMA);
            }
            exprs.add(parseExpr());
        }
        eat(TokenType.CLOSE_BRACKET);

        if (exprs.size() == 1) { // a bracketed expression
            return exprs.get(0);
        }else { // a tuple of 0, 2, 3, ... elements
            return new ASTTuple(exprs);
        }
    }

    // TODO: add grammar to each parse function's documentation
    private ASTMatchable parsePattern() {

        switch (currentToken.type) {
            case NAME:
                return parseName();

            case UNDERSCORE:
                eat(TokenType.UNDERSCORE);
                return new ASTUnderscore();

            case INTEGER:
                return parseInt();

            case OPEN_BRACKET:
                return parseBracketedPattern();

            default:
                throw new RuntimeException("parse error in pattern");
        }
    }

    private ASTMatchable parseBracketedPattern() {
        eat(TokenType.OPEN_BRACKET);

        List<ASTMatchable> patterns = new ArrayList<>();

        while (currentToken.type != TokenType.CLOSE_BRACKET) {
            if (patterns.size() > 0) {
                eat(TokenType.COMMA);  // a comma before each list element except the first
            }
            patterns.add(parsePattern());
        }
        eat(TokenType.CLOSE_BRACKET);

        if (patterns.size() == 1) { // just a random pair of brackets, ignore
            // TODO: matching against constructors here
            return patterns.get(0);

        }else { // a tuple
            // TODO: separate tuplePattern and tupleConstructor types would make this cleaner
            // List<B> is not a subtype of List<A>, even when B is a subtype of A
            // so cannot just: return new ASTTuple(elements);

            List<ASTNode> elements = new ArrayList<>(patterns.size());
            elements.addAll(patterns);
            return new ASTTuple(elements);
        }
    }

    private ASTLiteralInt parseInt() {
        Token t = currentToken;
        eat(TokenType.INTEGER);
        return new ASTLiteralInt(Integer.parseInt(t.string));
    }

    private ASTVar parseName() {
        Token t = currentToken;
        eat(TokenType.NAME);
        return new ASTVar(t.string);
    }


}
