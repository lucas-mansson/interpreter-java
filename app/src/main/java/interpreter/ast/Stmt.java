package interpreter.ast;

import java.util.List;
import interpreter.scanner.Token;

public abstract class Stmt {

    public interface Visitor<R> {

        public R visitBlock(Block stmt);

        public R visitExprStmt(ExprStmt stmt);

        public R visitIf(If stmt);

        public R visitPrint(Print stmt);

        public R visitVar(Var stmt);
    }

    public static class Block extends Stmt {

        public final List<Stmt> statements;

        public Block(List<Stmt> statements) {
            this.statements = statements;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlock(this);
        }
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

    public static class If extends Stmt {

        public final Expr condition;
        public final Stmt thenBranch;
        public final Stmt elseBranch;

        public If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIf(this);
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

    public static class Var extends Stmt {

        public final Token name;
        public final Expr initializer;

        public Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVar(this);
        }
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
