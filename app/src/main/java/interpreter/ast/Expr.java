package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

public abstract class Expr {

    public interface Visitor<R> {

        public R visitBinary(Binary expr);

        public R visitConditional(Conditional expr);

        public R visitGrouping(Grouping expr);

        public R visitLiteral(Literal expr);

        public R visitUnary(Unary expr);
    }

    public static class Binary extends Expr {

        public final Expr left;
        public final Token operator;
        public final Expr right;

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

    public static class Conditional extends Expr {

        public final Expr condition;
        public final Expr then;
        public final Expr els;

        public Conditional(Expr condition, Expr then, Expr els) {
            this.condition = condition;
            this.then = then;
            this.els = els;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitConditional(this);
        }
    }

    public static class Grouping extends Expr {

        public final Expr expr;

        public Grouping(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGrouping(this);
        }
    }

    public static class Literal extends Expr {

        public final Object value;

        public Literal(Object value) {
            this.value = value;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Unary extends Expr {

        public final Token operator;
        public final Expr right;

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
