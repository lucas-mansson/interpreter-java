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
    public String visitBinaryExpr(Expr.BinaryExpr expr) {
        return printRPN(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitLiteralExpr(Expr.LiteralExpr expr) {
        if (expr.value == null) {
            return "null";
        }
        return expr.value.toString();
    }

    @Override
    public String visitGroupingExpr(Expr.GroupingExpr expr) {
        return printRPN("group", expr);
    }

    @Override
    public String visitUnaryExpr(Expr.UnaryExpr expr) {
        return printRPN(expr.operator.lexeme(), expr.right);
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
        Expr expr = new Expr.BinaryExpr(
                new Expr.BinaryExpr(new Expr.LiteralExpr(1), new Token(TokenType.PLUS, "+", null, 1),
                        new Expr.LiteralExpr(2)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.BinaryExpr(new Expr.LiteralExpr(4), new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.LiteralExpr(3)));
        System.out.println(new RPNVisitor().print(expr));
    }
}
