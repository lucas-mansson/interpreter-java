package interpreter.ast;

import interpreter.scanner.*;
import interpreter.scanner.TokenType;

public class AstPrinter implements Expr.Visitor<String> {

    private String indent;

    public String print(Expr expr) {
        indent = "";
        return expr.accept(this);
    }

    @Override
    public String visitBinary(Expr.Binary expr) {
        String prevIndent = indent;
        StringBuilder sb = new StringBuilder();

        sb.append(expr.operator.lexeme);
        sb.append('\n');

        indent += "    ";
        sb.append(indent);
        sb.append(expr.left.accept(this));
        sb.append('\n');
        sb.append(indent);
        sb.append(expr.right.accept(this));

        indent = prevIndent;
        return sb.toString();
    }

    public String visitConditional(Expr.Conditional expr) {
        String prevIndent = indent;
        StringBuilder sb = new StringBuilder();

        sb.append("conditional");
        sb.append('\n');
        indent += "    ";

        sb.append(indent);
        sb.append("condition: " + expr.condition.accept(this));
        sb.append('\n');

        sb.append(indent);
        sb.append("then: " + expr.then.accept(this));
        sb.append('\n');

        sb.append(indent);
        sb.append("else: " + expr.els.accept(this));

        indent = prevIndent;
        return sb.toString();
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        if (expr.value == null) {
            return "null";
        }
        return expr.value.toString();
    }

    public String visitVariable(Expr.Variable var) {
        return var.name.lexeme;
    }

    @Override
    public String visitGrouping(Expr.Grouping expr) {
        String prevIndent = indent;
        StringBuilder sb = new StringBuilder();

        sb.append("group");
        sb.append('\n');

        indent += "    ";
        sb.append(indent);
        sb.append(expr.expr.accept(this));

        indent = prevIndent;
        return sb.toString();
    }

    @Override
    public String visitUnary(Expr.Unary expr) {
        String prevIndent = indent;
        StringBuilder sb = new StringBuilder();

        sb.append(expr.operator.lexeme);
        sb.append('\n');

        sb.append(indent);
        sb.append(expr.right.accept(this));

        indent = prevIndent;
        return sb.toString();
    }

    /*
     * private String parenthesize(String name, Expr... exprs) {
     * StringBuilder sb = new StringBuilder();
     * 
     * sb.append(name);
     * for (Expr expr : exprs) {
     * sb.append('\n');
     * sb.append(indent);
     * sb.append(expr.accept(this));
     * }
     * return sb.toString();
     * }
     */

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
