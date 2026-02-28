package interpreter;

import static interpreter.scanner.TokenType.values;

import java.util.HashMap;
import java.util.Map;

import interpreter.errors.RuntimeError;
import interpreter.scanner.Token;

class Environment {

    private final Map<String, Object> values = new HashMap<>();

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }

}
