package interpreter;

import java.util.HashMap;
import java.util.Map;

import interpreter.errors.RuntimeError;
import interpreter.scanner.Token;

class Environment {

    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    // Value given to uninitialized values
    public static final Object UNINITIALIZED = new Object();

    // Global scope
    public Environment() {
        enclosing = null;
    }

    // For nested scopes
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            Object value = values.get(name.lexeme);
            if (value == UNINITIALIZED) {
                throw new RuntimeError(name, "Use of uninitialized variable '" + name.lexeme + "'");
            }
            return value;
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable " + name.lexeme);
    }

}
