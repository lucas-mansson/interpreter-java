package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

public abstract class Stmt {

    public interface Visitor<R> {

        public R visitExpression(Expression stmt);

        public R visitPrint(Print stmt);
    }

    public static class Expression extends Stmt {

        public final Expr expression;

        public Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpression(this);
        }
    }

    public static class Print extends Stmt {

        public final Expr expression;

        public Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrint(this);
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
