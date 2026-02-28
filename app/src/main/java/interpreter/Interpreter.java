package interpreter;

import java.util.List;

import interpreter.ast.Expr;
import interpreter.ast.Stmt;
import interpreter.scanner.Token;
import interpreter.errors.RuntimeError;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private Environment environment = new Environment();

    public void interpret(List<Stmt> stmts) {
        try {
            for (Stmt stmt : stmts) {
                execute(stmt);
            }
        } catch (RuntimeError err) {
            Lang.runtimeError(err);
        }
    }

    @Override
    public Void visitExprStmt(Stmt.ExprStmt stmt) {
        eval(stmt.expr);
        return null;
    }

    @Override
    public Void visitPrint(Stmt.Print stmt) {
        Object val = eval(stmt.expr);
        System.out.println(stringify(val));
        return null;
    }

    @Override
    public Void visitBlock(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitVar(Stmt.Var stmt) {
        Object value = Environment.UNINITIALIZED;
        if (stmt.initializer != null) {
            value = eval(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Object visitAssign(Expr.Assign expr) {
        Object val = eval(expr.value);
        environment.assign(expr.name, val);
        return val;
    }

    @Override
    public Object visitVariable(Expr.Variable var) {
        return environment.get(var.name);
    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGrouping(Expr.Grouping expr) {
        return eval(expr.expr);
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
                if (right.equals(0.0)) {
                    throw new RuntimeError(expr.operator, "Cannot divide by 0");
                }
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

    public Object eval(Expr expr) {
        return expr.accept(this);
    }

    public void execute(Stmt stmt) {
        stmt.accept(this);
    }

    private void executeBlock(List<Stmt> stmts, Environment env) {
        Environment prev = this.environment;
        try {
            this.environment = env;
            for (Stmt s : stmts) {
                execute(s);
            }

        } finally {
            this.environment = prev;
        }
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
