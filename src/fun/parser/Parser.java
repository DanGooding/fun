package fun.parser;

import fun.ast.*;
import fun.lexer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Parser {

    // TODO: this can change as user defined operators are added
    private static final Map<String, OpInfo> operatorInfo =
        Map.of(
            "^", new OpInfo(8, Assoc.RIGHT),

            "*", new OpInfo(7, Assoc.LEFT),

            "+", new OpInfo(6, Assoc.LEFT),
            "-", new OpInfo(6, Assoc.LEFT),

            ":", new OpInfo(5, Assoc.RIGHT),

            "<", new OpInfo(4, Assoc.NONE),
            "==", new OpInfo(4, Assoc.NONE)
        );


    private final TokenStream tokenStream;
    private Token currentToken;

    public Parser(String input) {
        tokenStream = new BlockMaker(new Tokenizer(input));
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
        } else {
            // TODO: proper exception type (checked?)
            // the expected token is possibly not the most useful thing, eg:
            // let t = (1, 2 in t
            // gives 'expected COMMA', rather than close bracket
            // TODO: include position in source -- show code?
            throw new RuntimeException(String.format("parse error, %s expected", type));
        }
    }

    // TODO: top level: multiple expressions - this requires changes to the AST too

    // TODO: proper ParseErrorException ?

    // TODO: throw if EOF not reached at end (eg some invalid stuff before EOF)

    // TODO: parse methods that take parsers: ??
    //      <T> T parseBracketed(Supplier<T> parseElement)

    // TODO: add grammar to each parse function's documentation

    public ASTNode parseExpr() {

        switch (currentToken.type) {

            case LET: {
                eat(TokenType.LET);
                ASTMatchable pattern = parseBasePattern();
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
                ASTMatchable param = parseBasePattern();
                eat(TokenType.ARROW);
                ASTNode body = parseExpr();
                return new ASTLambda(param, body);
            }

            case CASE: {
                eat(TokenType.CASE);
                ASTNode subject = parseExpr();
                eat(TokenType.OF);

                List<ASTCaseOption> options =
                    parseDelimited(
                        () -> {
                            ASTMatchable pattern = parseOpenPattern();
                            eat(TokenType.ARROW);
                            ASTNode body = parseExpr();
                            return new ASTCaseOption(pattern, body);
                        },
                        TokenType.BLOCK_BEGIN,
                        TokenType.BLOCK_DELIM,
                        TokenType.BLOCK_END);

                return new ASTCase(subject, options);
            }

            default: {
                if (currentBeginsBaseExpr()) {
                    return parseOperatorSequence();

                } else {
                    throw new RuntimeException("parse error in expression");
                }
            }
        }
    }

    private ASTNode parseOperatorSequence() {
        // expr = ...
        //      | app OPERATOR expr
        //      | app

        List<String> operatorStack = new ArrayList<>(); // a specific type ?
        List<ASTNode> exprStack = new ArrayList<>();

        exprStack.add(parseApplication());

        while (currentToken.type == TokenType.OPERATOR) {
            String currentOp = currentToken.string;
            if (!operatorInfo.containsKey(currentOp)) {
                throw new RuntimeException("unknown operator " + currentOp);
            }
            eat(TokenType.OPERATOR);

            while (shouldReduceOperatorStack(operatorStack, currentOp)) {
                reduceOperatorStack(operatorStack, exprStack);
            }

            operatorStack.add(currentOp);

            if (currentBeginsBaseExpr()) {
                // an app, then possibly more
                exprStack.add(parseApplication());

            } else {
                // no app, but must have some expr to follow the previous operator
                // the operator sequence ends after this
                exprStack.add(parseExpr());
                break;
            }
        }

        // reduce to one expr
        while (operatorStack.size() > 0) {
            reduceOperatorStack(operatorStack, exprStack);
        }

        return exprStack.get(0);
    }

    private boolean shouldReduceOperatorStack(List<String> operatorStack, String currentOp) {
        // prevOp is top of stack, currentOp is about to be pushed
        if (operatorStack.size() == 0) return false;

        String prevOp = operatorStack.get(operatorStack.size() - 1);

        OpInfo prevInfo = operatorInfo.get(prevOp);
        OpInfo currentInfo = operatorInfo.get(currentOp);

        if (prevInfo.precedence > currentInfo.precedence) {
            return true;

        } else if (prevInfo.precedence < currentInfo.precedence) {
            return false;

        } else if (prevInfo.associativity != currentInfo.associativity) {
            throw new RuntimeException(String.format(
                "cannot mix %s (%s) and %s (%s)",
                prevOp, prevInfo,
                currentOp, currentInfo));

        } else if (prevInfo.associativity == Assoc.NONE) { // both NONE
            throw new RuntimeException(String.format(
                "cannot mix non-associative %s and %s",
                prevOp,
                currentOp));

        } else {
            // reduce for LEFT, not for RIGHT
            return prevInfo.associativity == Assoc.LEFT;
        }
    }

    private void reduceOperatorStack(List<String> operatorStack, List<ASTNode> exprStack) {
        // pop top two exprs as operands
        ASTNode right = exprStack.remove(exprStack.size() - 1);
        ASTNode left = exprStack.remove(exprStack.size() - 1);

        // pop top operator
        String op = operatorStack.remove(operatorStack.size() - 1);

        // TODO: temporary until treat operators as functions
        ASTNode combined;
        switch (op) {
            case "+":
                combined = new ASTPlus(left, right);
                break;

            case "-":
                combined = new ASTMinus(left, right);
                break;

            case "*":
                combined = new ASTMult(left, right);
                break;

            case "^":
                combined = new ASTPow(left, right);
                break;

            case "<":
                combined = new ASTLessThan(left, right);
                break;

            case "==":
                combined = new ASTEquals(left, right);
                break;

            case ":":
                combined = new ASTListCons(left, right);
                break;

            default:
                throw new RuntimeException("cannot parse operator " + op);
        }

        exprStack.add(combined);
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
            case TRUE:
            case FALSE:
            case NAME:
            case OPEN_BRACKET:
            case OPEN_SQUARE_BRACKET:
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

            case TRUE:
            case FALSE:
                return parseBool();

            case NAME:
                return parseName();

            case OPEN_BRACKET:
                return parseBracketedExpr();

            case OPEN_SQUARE_BRACKET:
                return parseListLiteral();

            default:
                throw new RuntimeException("parse error in (base) expression");
        }

    }


    private <T> List<T> parseDelimited(Supplier<T> parseElement, TokenType start, TokenType delimiter, TokenType end) {
        List<T> elements = new ArrayList<>();

        eat(start);
        while (currentToken.type != end) {
            if (elements.size() > 0) {
                eat(delimiter);
            }
            elements.add(parseElement.get());
        }
        eat(end);

        return elements;
    }

    private ASTNode parseBracketedExpr() {
        List<ASTNode> exprs = parseDelimited(
            this::parseExpr,
            TokenType.OPEN_BRACKET, TokenType.COMMA, TokenType.CLOSE_BRACKET);

        if (exprs.size() == 1) { // a bracketed expression
            return exprs.get(0);
        } else { // a tuple of 0, 2, 3, ... elements
            return new ASTTuple(exprs);
        }
    }

    private ASTNode parseListLiteral() {
        List<ASTNode> elements = parseDelimited(
            this::parseExpr,
            TokenType.OPEN_SQUARE_BRACKET, TokenType.COMMA, TokenType.CLOSE_SQUARE_BRACKET);

        ASTNode list = new ASTListNil();
        for (int i = elements.size() - 1; i >= 0; i--) {
            list = new ASTListCons(elements.get(i), list);
        }
        return list;
    }

    private ASTMatchable parseBasePattern() {
        // basePattern = NAME | UNDERSCORE
        //             | INTEGER | TRUE | FALSE
        //             | ( openPattern )
        //             | ( openPattern , ... , openPattern )
        //             | [ openPattern , ... , openPattern ]

        switch (currentToken.type) {
            case NAME:
                return parseName();

            case UNDERSCORE:
                eat(TokenType.UNDERSCORE);
                return new ASTUnderscore();

            case INTEGER:
                return parseInt();

            case TRUE:
            case FALSE:
                return parseBool();

            case OPEN_BRACKET:
                return parseBracketedPattern();

            case OPEN_SQUARE_BRACKET:
                return parseListPattern();

            default:
                throw new RuntimeException("parse error in pattern");
        }

    }

    private ASTMatchable parseOpenPattern() {
        // openPattern = basePattern : openPattern
        //             | basePattern

        // TODO: use operator stack parser here too

        ASTMatchable firstPattern = parseBasePattern();
        if (currentToken.type != TokenType.OPERATOR) {
            return firstPattern;
        }

        List<ASTMatchable> patterns = new ArrayList<>();
        patterns.add(firstPattern);

        while (currentToken.type == TokenType.OPERATOR) {
            if (!currentToken.string.equals(":")) {
                throw new RuntimeException(
                    String.format("unexpected operator in pattern: %s", currentToken.string));
            }
            eat(TokenType.OPERATOR);

            patterns.add(parseBasePattern());
        }

        // build up cons pattern (last pattern is list, rest are elements)
        ASTMatchable listPattern = patterns.remove(patterns.size() - 1);
        while (!patterns.isEmpty()) {
            listPattern = new ASTListConsPattern(
                patterns.remove(patterns.size() - 1),
                listPattern);
        }

        return listPattern;
    }

    private ASTMatchable parseBracketedPattern() {
        // bracketedPattern = ( openPattern )
        //                  | ( openPattern , ... )

        List<ASTMatchable> patterns =
            parseDelimited(this::parseOpenPattern, TokenType.OPEN_BRACKET, TokenType.COMMA, TokenType.CLOSE_BRACKET);

        if (patterns.size() == 1) { // just a random pair of brackets, ignore
            // TODO: matching against constructors here
            return patterns.get(0);

        } else { // a tuple
            return new ASTTuplePattern(patterns);
        }
    }

    private ASTMatchable parseListPattern() {
        // listPattern = [ openPattern , ... ]

        List<ASTMatchable> elements =
            parseDelimited(
                this::parseOpenPattern,
                TokenType.OPEN_SQUARE_BRACKET, TokenType.COMMA, TokenType.CLOSE_SQUARE_BRACKET);

        ASTMatchable pattern = new ASTListNil();
        for (int i = elements.size() - 1; i >= 0; i--) {
            pattern = new ASTListConsPattern(elements.get(i), pattern);
        }
        return pattern;
    }


    private ASTLiteralBool parseBool() {
        if (currentToken.type == TokenType.TRUE) {
            eat(TokenType.TRUE);
            return new ASTLiteralBool(true);
        }else {
            eat(TokenType.FALSE);
            return new ASTLiteralBool(false);
        }
    }

    private ASTLiteralInteger parseInt() {
        Token t = currentToken;
        eat(TokenType.INTEGER);
        return new ASTLiteralInteger(t.string);
    }

    private ASTVar parseName() {
        Token t = currentToken;
        eat(TokenType.NAME);
        return new ASTVar(t.string);
    }


}
