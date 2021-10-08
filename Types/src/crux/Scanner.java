package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class Scanner implements Iterable<Token> {
	public static String studentName = "Bum Keun Cho";
	public static String studentID = "71092926";
	public static String uciNetID = "bkcho";

	private final List<Token> tokens = new ArrayList<>();
	private int lineNum;  // current line count
	private int charPos;  // character offset for current line
	private int nextChar; // contains the next char (-1 == EOF)
	private Reader input;

	private final int NEWLINE = 10;
	private final int EOF = -1;

	Scanner(Reader reader)
	{
		// TODO: initialize the Scanner
		input = reader;
		lineNum = 1;
		charPos = 0;
		nextChar = readChar();
	}

	// OPTIONAL: helper function for reading a single char from input
	//           can be used to catch and handle any IOExceptions,
	//           advance the charPos or lineNum, etc.
	private int readChar() {
		int x = 0;
		try {
			x = input.read();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		charPos++;
		return x;
	}

	/* Invariants:
	 *  1. call assumes that nextChar is already holding an unread character
	 *  2. return leaves nextChar containing an untokenized character
	 */
	public Token next()
	{
		// TODO: implement this
		while (Character.isWhitespace(nextChar)) {
			if(nextChar == EOF) {
				break;
			}
			clearEmptySpace();
		}
		String s = String.valueOf((char) nextChar);
		return (nextChar == EOF) ? Token.EOF(lineNum, charPos) : stateMachine(s);
	}

	// OPTIONAL: any other methods that you find convenient for implementation or testing
	private Token stateMachine(String s) {
		final int EQUALS = 61;
		final int COLON = 58;
		nextChar = readChar();
		do {
			s = checkComments(s, nextChar);
		}
		while (s.equals("/") && nextChar == '/');
		if (s.equals("EOF")) {
			return Token.EOF(lineNum, charPos);
		}
		if (alphanumeric(s)) {
			return determineKind(s);
		}
		else if (special(s)) {
			if ((comparator(s) && nextChar == EQUALS) || (s.equals(":") && nextChar == COLON)) {
				nextChar = readChar();
				s += s.equals(":") ? ":" : "=";
				return new Token(s, lineNum, charPos - s.length());
			}
			else {
				return new Token(s, lineNum, charPos - s.length());
			}
		}
		else {
			return new Token(s, lineNum, charPos - s.length());
		}
	}

	private Token determineKind(String s) {
		char[] c = s.toCharArray();
		return digit(c[0]) ? getNumber(s) : getString(s);
	}

	private Token getNumber(String s) {
		final int DECIMAL = 46;
		while (nextChar == DECIMAL || digit((char) nextChar)) {
			if (nextChar == DECIMAL) {
				s += (char) nextChar;
				nextChar = readChar();
				return getFloat(s);
			}
			s += (char) nextChar;
			nextChar = readChar();
		}
		return Token.INTEGER(s, lineNum, charPos - s.length());
	}

	private Token getString(String s) {
		while (digit((char) nextChar) || letter((char) nextChar)) {
			s += (char) nextChar;
			nextChar = readChar();
		}
		Token t = new Token(s, lineNum, charPos - s.length());
		if (t.is(Token.Kind.ERROR)) {
			t = Token.IDENTIFIER(s, lineNum, charPos - s.length());
		}
		return t;
	}

	private Token getFloat(String s) {
		while (digit((char) nextChar)) {
			s += (char) nextChar;
			nextChar = readChar();
		}
		return Token.FLOAT(s, lineNum, charPos - s.length());
	}

	private boolean letter(char c) {
		if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
			return true;
		}
		return false;
	}

	private boolean digit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}

	private boolean alphanumeric(String s) {
		char[] c = s.toCharArray();
		return letter(c[0]) || digit(c[0]);
	}

	private boolean comparator(String s) {
		if (s.equals(">") || s.equals("<") || s.equals("=") || s.equals("!") || s.equals(":")) {
			return true;
		}
		return false;
	}

	private boolean special(String s) {
		if (comparator(s) || s.equals(":")) {
			return true;
		}
		return false;
	}

	private String checkComments(String s, int next) {
		if (s.equals("/") && (char) next == '/') {
			while (nextChar != NEWLINE) {
				nextChar = readChar();
				if (nextChar == EOF) {
					break;
				}
			}
			String remainder = "";
			if (nextChar == EOF) {
				remainder = "EOF";
			}
			else {
				clearEmptySpace();
				if (nextChar != EOF) {
					remainder += String.valueOf((char) nextChar);
					nextChar = readChar();
				}
				else {
					remainder = "EOF";
				}
			}
			return remainder;
		}
		else {
			return s;
		}
	}

	private void clearEmptySpace() {
		if (nextChar == NEWLINE) {
			while(nextChar == NEWLINE) {
				lineNum++;
				charPos = 0;
				nextChar = readChar();
			}
		}
		while (Character.isWhitespace(nextChar)) {
			if(nextChar == NEWLINE || nextChar == EOF) {
				break;
			}
			nextChar = readChar();
		}
	}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}
}