# interpreter

This is the code for a simple interpreted programming language based on the book ["Crating interpreters" by Robert Nystrom.](https://craftinginterpreters.com/). 
I try to add my own ideas and things so its not a 1:1 copy.

The language is high-level, dynamically typed, and garbage collected, supporting scripting, procedural programming and basic object oriented concepts. You can read more about the language below.

## Things to add:

Features I would like that are not in the book:

- [ ] Differentiate integer and floating point numbers
- [ ] Ternary operator
- [ ] Differentiate const and var
- [ ] private/public data and methods
- [ ] do-while
- [ ] Static type checking
- [ ] arrays and [] operator
- [ ] Add multi-line comments
- [ ] Add support for leading decimal point e.g. `.123`
- [ ] ...


### Standard library

The language has no Standard Library basically. Here are some things to add:

- [ ] Print function
- [ ] Read from command line
- [ ] File I/O
- [ ] Networking
- [ ] ...


## Language specification

### Primitive types

- Booleans
- Numbers (double-precision floating point)
- Strings
- null

### Grammar
```
program     -> declaration* EOF ;

declaration -> varDecl
               | stmt;

stmt        -> exprStmt 
               | printStmt ;

varDecl     -> "var" ID ( "=" expr )? ";" ;

exprStmt    -> expr ";" ;

printStmt   -> "print" expr ";" ;

expr        -> assignment ("," assignment)* ;

assignment  -> conditional
               | ID "=" assignment ;

conditional -> equality 
            | equality "?" expr ":" expr;

equality    -> comparison ( ( "!=" | "==" ) comparison )* ;

comparison  -> term ( ( ">" | ">=" | "<" | "<=" ) term )* ;

term        -> factor ( ( "-" | "+" ) ) factor)* ;

factor      -> unary ( ( "/" | "*" ) unary )* ;

unary       -> ( "!" | "-" ) unary 
               | primary ;

primary     -> NUMBER 
               | STRING 
               | "true" 
               | "false" 
               | "null" 
               | "(" expression ")" 
               | ID ;
```

### Tokens

The language has the following tokens (defined in `TokenType.java`):
```c
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
    COLON, // ":"

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
```

