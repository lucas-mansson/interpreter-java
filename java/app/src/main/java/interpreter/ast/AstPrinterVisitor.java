package interpreter.ast;

class AstPrinterVisitor implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.BinaryExpr expr) {
        return parenthesize(expr.operator.lexeme(), expr.left, expr.right);
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
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitUnaryExpr(Expr.UnaryExpr expr) {
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
}
