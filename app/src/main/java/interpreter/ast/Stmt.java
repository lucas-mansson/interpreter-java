package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

public abstract class Stmt {

    public interface Visitor<R> {

        public R visitExprStmt(ExprStmt stmt);

        public R visitPrint(Print stmt);
    }

    public static class ExprStmt extends Stmt {

        public final Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExprStmt(this);
        }
    }

    public static class Print extends Stmt {

        public final Expr expr;

        public Print(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrint(this);
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
