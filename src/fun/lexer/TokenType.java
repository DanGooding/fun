package fun.lexer;

public enum TokenType {
    INTEGER,

    NAME,

    // keywords:
    LET,
    IN,

    IF,
    THEN,
    ELSE,

    LAMBDA,

    CASE,
    OF,

    BLOCK_BEGIN,
    BLOCK_DELIM,
    BLOCK_END,

    // literals
    TRUE,
    FALSE,

    OPERATOR,

    ASSIGN_EQUALS,

    ARROW,  // ->

    OPEN_BRACKET,
    CLOSE_BRACKET,

    OPEN_SQUARE_BRACKET,
    CLOSE_SQUARE_BRACKET,

    COMMA,

    UNDERSCORE,

    NEWLINE,

    EOF

}
