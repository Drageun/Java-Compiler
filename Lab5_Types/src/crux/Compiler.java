package crux;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.PrintWriter;

public class Compiler {
    public static String studentName = "Bum Keun Cho";
    public static String studentID = "71092926";
    public static String uciNetID = "bkcho";

	public static void main(String[] args)
	{
        String sourceFilename = args[0];
        sourceFilename = "tests/test" + "13" + ".crx"; //DELETE BEFORE SUBMISSION
        Scanner s = null;

        try {
            s = new Scanner(new FileReader(sourceFilename));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error accessing the source file: \"" + sourceFilename + "\"");
            System.exit(-2);
        }

        Parser p = new Parser(s);
        ast.Command syntaxTree = p.parse();
        if (p.hasError()) {
            System.out.println("Error parsing file " + sourceFilename);
            System.out.println(p.errorReport());
            System.exit(-3);
        }

        types.TypeChecker tc = new types.TypeChecker();
        tc.check(syntaxTree);
        if (tc.hasError()) {
            System.out.println("Error type-checking file.");
            System.out.println(tc.errorReport());
            System.exit(-4);
        }
        System.out.println("Crux Program has no type errors.");
    }
}