package interpreter;

import interpreter.parser.Parser;
import interpreter.scanner.Scanner;
import interpreter.scanner.Token;
import interpreter.scanner.TokenType;
import interpreter.ast.AstPrinter;
import interpreter.ast.Expr;
import interpreter.ast.Stmt;
import interpreter.errors.RuntimeError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lang {

    private static final Interpreter interpreter = new Interpreter();

    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("usage: script [script]");
            // unix sysexits.h: EX_USAGE 64 - command line usage error
            System.exit(64);
        } else if (args.length == 1) {
            // Run from source file
            runFile(args[0]);
        } else {
            // REPL, read evaluate print loop
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            // unix sysexits.h: EX_DATAERR 65 - data format error
            System.exit(65);
        }
        if (hadRuntimeError) {
            // unix sysexits.h: EX_SOFTWARE 70 - internal software error
            System.exit(70);
        }
    }

    private static void run(String source) {
        // not to be confused with java.util.Scanner
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> stmts = parser.parse();

        if (hadError) {
            return;
        }
        for (Stmt s : stmts) {
            if (s instanceof Stmt.ExprStmt) {
                System.out.println(interpreter.eval(((Stmt.ExprStmt) s).expr));
            } else {
                interpreter.execute(s);
            }
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
            System.out.print(">");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            // reset error after each line in REPL to avoid crashing
            hadError = false;
        }
    }

    public static void error(int line, int column, String message) {
        report(line, column, "", message);
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, token.col, " at end", message);
        } else {
            report(token.line, token.col, " at '" + token.lexeme + "'", message);
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() + "\n[line " + error.token.line + ":" + error.token.col + "]");
        hadRuntimeError = true;
    }

    private static void report(int line, int column, String where, String message) {
        System.err.println("[line " + line + ":" + column + "] Error" + where + ": " + message);
        hadError = true;
    }
}
