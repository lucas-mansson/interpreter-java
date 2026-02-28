package interpreter.ast;

import interpreter.scanner.*;
import interpreter.scanner.TokenType;

// This class prints a expr in RPN format
// RPN: Reverse polish notation, operands are placed before operator
class RPNVisitor implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinary(Expr.Binary expr) {
        return printRPN(expr.operator.lexeme, expr.left, expr.right);
    }

    public String visitConditional(Expr.Conditional expr) {
        return printRPN("conditional", expr.condition, expr.then, expr.els);
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
        return printRPN("group", expr);
    }

    @Override
    public String visitUnary(Expr.Unary expr) {
        return printRPN(expr.operator.lexeme, expr.right);
    }

    public String printRPN(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();
        for (Expr e : exprs) {
            sb.append(e.accept(this));
            sb.append(" ");
        }
        sb.append(name);
        return sb.toString();
    }

    public static void main(String[] args) {
        Expr expr = new Expr.Binary(
                new Expr.Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1, 1),
                        new Expr.Literal(2)),
                new Token(TokenType.STAR, "*", null, 1, 1),
                new Expr.Binary(new Expr.Literal(4), new Token(TokenType.MINUS, "-", null, 1, 1),
                        new Expr.Literal(3)));
        System.out.println(new RPNVisitor().print(expr));
    }
}
