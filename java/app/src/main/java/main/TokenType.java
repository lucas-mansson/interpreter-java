package main;

enum TokenType {
    // Single character tokens
    LPAREN, // "("
    RPAREN, // ")"
    LBRACE, // "{"
    RBRACE, // "}"
    COMMA, // ","
    DOT, // "."
    MINUS, // "-"
    PLUS, // "+"
    SEMICOL, // ";"
    SLASH, // "/"
    STAR, // "*"

    // Comparison operators
    BANG, // "!"
    BANG_EQ, // "!="
    EQ, // "="
    EQ_EQ, // "=="
    GT, // ">"
    GE, // ">="
    LT, // "<"
    LE, // "<="
    AND, // "&&"
    OR, // "||"

    // Literals
    ID,
    STRING,
    NUMBER,

    // Keywords
    VAR, // "var"
    IF, // "if"
    ELSE, // "else"
    FOR, // "for"
    WHILE, // "while"
    FALSE, // "false"
    TRUE, // "true"
    FUN, // "fun"
    RETURN, // "return"
    NULL, // "null"
    CLASS, // "class"
    THIS, // "this"
    SUPER, // "super"
    PRINT, // "print"

    EOF
}
