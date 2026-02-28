package interpreter.parser;

import java.util.List;

import interpreter.Lang;
import interpreter.ast.Expr;
import interpreter.scanner.Token;
import interpreter.scanner.TokenType;
import static interpreter.scanner.TokenType.*;

// Recursive descent parser
public class Parser {
    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        try {
            return expression();
        } catch (ParseError e) {
            return null;
        }
    }

    private Expr expression() {
        Expr expr = conditional();
        while (match(COMMA)) {
            Token operator = prev();
            Expr right = conditional();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr conditional() {
        Expr expr = equality();
        if (match(QUESTIONMARK)) {
            Expr then = expression();
            consume(COLON, "Expect ':' after expression");
            Expr elseExpr = expression();
            expr = new Expr.Conditional(expr, then, elseExpr);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(BANG_EQ, EQ_EQ)) {
            Token operator = prev();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(GT, GE, LT, LE)) {
            Token operator = prev();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(MINUS, PLUS)) {
            Token operator = prev();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(SLASH, STAR)) {
            Token operator = prev();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = prev();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(NULL))
            return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(prev().literal);
        }

        if (match(LPAREN)) {
            Expr expr = expression();
            consume(RPAREN, "Expect ')' after expression");
            return new Expr.Grouping(expr);
        }
        throw error(peek(), "Expression expected.");
    }

    // Parsing utility functions
    private boolean match(TokenType... types) {
        for (TokenType t : types) {
            if (check(t)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType t) {
        if (isAtEnd()) {
            return false;
        }
        return peek().type == t;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return prev();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token prev() {
        return tokens.get(current - 1);
    }

    // Error reporting and handling
    private Token consume(TokenType t, String msg) {
        if (check(t)) {
            return advance();
        }
        throw error(peek(), msg);
    }

    private ParseError error(Token t, String msg) {
        Lang.error(t, msg);
        return new ParseError();
    }

}
