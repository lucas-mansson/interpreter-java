package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

abstract class Expr {

    static class BinaryExpr extends Expr {
        BinaryExpr(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class GroupingExpr extends Expr {
        GroupingExpr(Expr expression) {
            this.expression = expression;
        }
        final Expr expression;
    }

    static class LiteralExpr extends Expr {
        LiteralExpr(Object value) {
            this.value = value;
        }
        final Object value;
    }

    static class UnaryExpr extends Expr {
        UnaryExpr(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
        final Token operator;
        final Expr right;
    }

}
