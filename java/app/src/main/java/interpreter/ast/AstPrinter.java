package interpreter.ast;

import interpreter.scanner.*;
import interpreter.scanner.TokenType;

public class AstPrinter implements Expr.Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinary(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        if (expr.value == null) {
            return "null";
        }
        return expr.value.toString();
    }

    @Override
    public String visitGrouping(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitUnary(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme(), expr.right);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(name);
        for (Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }
        sb.append(")");
        return sb.toString();
    }

    // example test function
    public static void main(String[] args) {
        Expr expr = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1, 1), new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1, 1),
                new Expr.Grouping(new Expr.Literal(45.67)));
        System.out.println(new AstPrinter().print(expr));
    }
}
