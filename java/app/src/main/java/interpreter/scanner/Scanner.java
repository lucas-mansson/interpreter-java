package interpreter.scanner;

import static interpreter.scanner.TokenType.*;

import java.io.PrintStream;
import interpreter.Interpreter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String src; // store source code as string
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0; // first char in lexeme
    private int current = 0; // current character
    private int line = 1;

    private static final Map<String, TokenType> kws;

    static {
        kws = new HashMap<>();
        kws.put("var", VAR);
        kws.put("if", IF);
        kws.put("else", ELSE);
        kws.put("for", FOR);
        kws.put("while", WHILE);
        kws.put("false", FALSE);
        kws.put("true", TRUE);
        kws.put("fun", FUN);
        kws.put("return", RETURN);
        kws.put("null", NULL);
        kws.put("class", CLASS);
        kws.put("this", THIS);
        kws.put("super", SUPER);
        kws.put("print", PRINT);
    }

    public Scanner(String src) {
        this.src = src;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line, start));
        return tokens;
    }

    public void printTokens(PrintStream out) {
        for (Token token : tokens) {
            out.println(token);
        }
    }

    private boolean isAtEnd() {
        return current >= src.length();
    }

    // advance gets input, scanToken produces output
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LPAREN);
                break;
            case ')':
                addToken(RPAREN);
                break;
            case '{':
                addToken(LBRACE);
                break;
            case '}':
                addToken(RBRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOL);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQ : BANG);
                break;
            case '=':
                addToken(match('=') ? EQ_EQ : EQ);
                break;
            case '>':
                addToken(match('=') ? GE : GT);
                break;
            case '<':
                addToken(match('=') ? LE : LT);
                break;
            case '&':
                // we dont have single '&' or '|' yet
                if (match('&')) {
                    addToken(AND);
                } else {
                    unexpectedCharError(c);
                }
                break;
            case '|':
                if (match('|')) {
                    addToken(OR);
                } else {
                    unexpectedCharError(c);
                }
                break;
            case '/':
                if (match('/')) {
                    // double-slash means comment until end of line
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else if (match('*')) {
                    // multi-line comment
                    while (!isAtEnd()) {
                        if (peek() == '*' && peekNext() == '/') {
                            advance();
                            advance();
                            break;
                        }
                        advance();
                    }
                } else {
                    addToken(SLASH); // otherwise its a operator
                }
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (Character.isDigit(c)) {
                    number();
                } else if (Character.isLetter(c)) {
                    identifier();
                } else {
                    unexpectedCharError(c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        TokenType token = kws.get(src.substring(start, current));
        if (token != null) {
            addToken(token);
        } else {
            addToken(ID);
        }
    }

    private boolean isAlphaNumeric(char c) {
        return Character.isDigit(c) || Character.isLetter(c);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            // Allow for multi-line strings
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        if (isAtEnd()) {
            Interpreter.error(line, current, "unterminated string");
            return;
        }
        // When we get here, we have the closing '"'
        advance();

        String val = src.substring(start + 1, current - 1);
        addToken(STRING, val);
    }

    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance();
            while (Character.isDigit(peek())) {
                advance();
            }
        }

        double val = Double.parseDouble(src.substring(start, current));
        addToken(NUMBER, val);
    }

    private boolean match(char expected) {
        if (isAtEnd() || src.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char peek() {
        // returns character without "consuming" it, i.e without incrementing.
        if (isAtEnd()) {
            return '\0'; // null terminator
        }
        return src.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= src.length()) {
            return '\0'; // null terminator
        }
        return src.charAt(current + 1);
    }

    private char advance() {
        // "Consumes" the character
        // returns current character and then increments
        return src.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = src.substring(start, current);
        tokens.add(new Token(type, text, literal, line, start));
    }

    private void unexpectedCharError(char c) {
        Interpreter.error(line, current, "Unexpected character: " + c);
    }
}
