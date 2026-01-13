package interpreter.scanner;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;
    final int col;

    public Token(TokenType type, String lexeme, Object literal, int line, int col) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.col = col;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }

    public String lexeme() {
        return this.lexeme;
    }

    public TokenType type() {
        return type;
    }

    public Object literal() {
        return literal;
    }

    public int line() {
        return line;
    }

    public int col() {
        return col;
    }
}
