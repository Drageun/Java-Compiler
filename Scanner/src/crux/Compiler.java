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
        String sourceFile = args[0];
        Scanner s = null;
        PrintWriter pw = null;

        try {
            s = new Scanner(new FileReader(sourceFile));
            pw = new PrintWriter(new FileWriter("out.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error accessing the source file: \"" + sourceFile + "\"");
            System.exit(-2);
        }

        Token t = s.next();
        while (!(t.is(Token.Kind.EOF))) {
                System.out.println(t);
                pw.println(t);
                t = s.next();
        }
        System.out.println(t);
        pw.println(t);
        pw.close();
    }
}