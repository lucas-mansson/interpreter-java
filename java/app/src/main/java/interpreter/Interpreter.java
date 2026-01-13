package interpreter;

import interpreter.ast.Expr;
import interpreter.scanner.Token;
import interpreter.errors.RuntimeError;

public class Interpreter implements Expr.Visitor<Object> {

    public void interpret(Expr expr) {
        try {
            Object val = eval(expr);
            System.out.println(stringify(val));
        } catch (RuntimeError err) {
            Lang.runtimeError(err);
        }
    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGrouping(Expr.Grouping expr) {
        return eval(expr.expression);
    }

    @Override
    public Object visitUnary(Expr.Unary expr) {
        Object right = eval(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                return -(double) right;
            default:
                break;
        }
        // unreachable
        return null;
    }

    @Override
    public Object visitConditional(Expr.Conditional expr) {
        Object condition = eval(expr.condition);
        if (isTruthy(condition)) {
            return eval(expr.then);
        }
        return eval(expr.els);
    }

    @Override
    public Object visitBinary(Expr.Binary expr) {
        Object left = eval(expr.left);
        Object right = eval(expr.right);

        switch (expr.operator.type) {
            // Conditionals
            case GT:
                ensureNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GE:
                ensureNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LT:
                ensureNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LE:
                ensureNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case BANG_EQ:
                ensureNumberOperands(expr.operator, left, right);
                return !isEqual(left, right);
            case EQ_EQ:
                ensureNumberOperands(expr.operator, left, right);
                return isEqual(left, right);

            // Arithmetic
            case MINUS:
                ensureNumberOperand(expr.operator, right);
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof String || right instanceof String) {
                    return stringify(left) + stringify(right);
                }
                throw new RuntimeError(expr.operator, "Operands must be of compatible types");
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;

            case COMMA:
                return right;

            default:
                break;
        }
        // unreachable
        return null;
    }

    private Object eval(Expr expr) {
        return expr.accept(this);
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;
        return a.equals(b);
    }

    private boolean isTruthy(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Boolean)
            return (boolean) obj;
        return true;
    }

    private void ensureNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private void ensureNumberOperands(Token operator, Object l, Object r) {
        if (l instanceof Double && r instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    private String stringify(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Double) {
            String text = obj.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return obj.toString();
    }
}
