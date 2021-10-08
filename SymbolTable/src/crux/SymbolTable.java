package crux;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SymbolTable {
    private LinkedHashMap<String, Symbol> symbols;
    private SymbolTable parent;
    private int depth;

    public SymbolTable()
    {
        symbols = new LinkedHashMap<String, Symbol>();
        parent = null;
        depth = 0;
    }

    public SymbolTable(int dep)
    {
        symbols = new LinkedHashMap<String, Symbol>();
        parent = null;
        this.depth = dep;
    }
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
        SymbolTable currentSymbolTable = this;
        if (currentSymbolTable.symbols.containsKey(name)) {
            return currentSymbolTable.symbols.get(name);
        }
        else {
            if(currentSymbolTable.parent != null) {
                return currentSymbolTable.parent.lookup(name);
            }
            throw new SymbolNotFoundError(name);
        }
    }

    public Symbol insert(String name) throws RedeclarationError
    {
        if (symbols.containsKey(name)) {
            throw new RedeclarationError(symbols.get(name));
        }
        else {
            symbols.put(name, new Symbol(name));
            return symbols.get(name);
        }
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (parent != null)
            sb.append(parent.toString());

        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }

        for (Symbol s : symbols.values())
        {
            sb.append(indent + s.toString() + "\n");
        }
        return sb.toString();
    }

    public boolean parentExists() {
        if (parent == null) {
            return false;
        }
        return true;
    }

    public void setParent(SymbolTable st) {
        parent = st;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void enterSymbol(String key, String value) {
        symbols.put(key, new Symbol(value));
    }

    public int getDepth() {
        return depth;
    }
}

class SymbolNotFoundError extends Error
{
    private static final long serialVersionUID = 1L;
    private String name;

    SymbolNotFoundError(String name)
    {
        this.name = name;
    }

    public String name()
    {
        return name;
    }
}

class RedeclarationError extends Error
{
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Symbol sym)
    {
        super("Symbol " + sym + " being redeclared.");
    }
}