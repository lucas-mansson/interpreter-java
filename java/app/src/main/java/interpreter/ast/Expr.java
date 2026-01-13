package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

public abstract class Expr {

    public interface Visitor<R> {

        public R visitBinary(Binary expr);

        public R visitGrouping(Grouping expr);

        public R visitLiteral(Literal expr);

        public R visitUnary(Unary expr);
    }

    public static class Binary extends Expr {

        final Expr left;
        final Token operator;
        final Expr right;

        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinary(this);
        }
    }

    public static class Grouping extends Expr {

        final Expr expression;

        public Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }

    public static class Literal extends Expr {

        final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {

        final Token operator;
        final Expr right;

        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
