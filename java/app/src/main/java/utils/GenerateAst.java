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
                "BinaryExpr: Expr left, Token operator, Expr right",
                "GroupingExpr: Expr expression",
                "LiteralExpr: Object value",
                "UnaryExpr: Token operator, Expr right"));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package interpreter.ast;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println("import interpreter.scanner.Token;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        writer.println();
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }
        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fields) {
        writer.println("    static class " + className + " extends " + baseName + " {");
        writer.println("        " + className + "(" + fields + ") {");

        String[] fieldArr = fields.split(", ");
        for (String field : fieldArr) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }
        writer.println("        }");
        for (String field : fieldArr) {
            writer.println("        final " + field + ";");
        }
        writer.println("    }");
        writer.println();
    }
}
