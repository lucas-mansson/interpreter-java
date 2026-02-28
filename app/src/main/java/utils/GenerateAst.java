package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// Utility class to generate classes to be used in the AST
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output_directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary: Expr left, Token operator, Expr right",
                "Conditional: Expr condition, Expr then, Expr els",
                "Grouping: Expr expression",
                "Literal: Object value",
                "Unary: Token operator, Expr right"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package interpreter.ast;");
        writer.println();

        writer.println("import java.util.List;");
        writer.println("import interpreter.scanner.Token;");
        writer.println();

        writer.println("public abstract class " + baseName + " {");

        writer.println();
        defineVisitor(writer, baseName, types);
        writer.println();

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // Base accept() method
        writer.println("    public abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fields) {
        // Type class definition
        writer.println("    public static class " + className + " extends " + baseName + " {");
        writer.println();

        // Fields
        String[] fieldArr = fields.split(", ");
        for (String field : fieldArr) {
            writer.println("        public final " + field + ";");
        }
        writer.println();

        // Constructor
        writer.println("        public " + className + "(" + fields + ") {");
        for (String field : fieldArr) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");

        // Visitor pattern
        writer.println();
        writer.println("        @Override");
        writer.println("        public <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + "(this);");
        writer.println("        }");

        writer.println("    }");
        writer.println();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    public interface Visitor<R> {");
        for (String type : types) {
            writer.println();
            String typeName = type.split(":")[0].trim();
            writer.println(
                    "        public R visit" + typeName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("    }");
    }
}
