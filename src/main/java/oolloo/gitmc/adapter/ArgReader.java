package oolloo.gitmc.adapter;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.eclipse.jgit.pgm.TextBuiltin;

import java.util.ArrayList;

public class ArgReader extends StringReader {

    public int length;
    public int pos;
    public boolean containHelp;
    public ArrayList<String> args;
    public ArrayList<Integer> argsPos;

    public ArgReader(StringReader other) throws CommandSyntaxException {
        super(other);
        if (other instanceof ArgReader) {
            length = ((ArgReader) other).length;
            pos = ((ArgReader) other).pos;
            containHelp = ((ArgReader) other).containHelp;
            args = ((ArgReader) other).args;
            argsPos = ((ArgReader) other).argsPos;
        } else {
            init();
        }
    }

    public ArgReader(String string) throws CommandSyntaxException {
        super(string);
        init();
    }

    public ArgReader() {
        super("");
        length = 0;
        pos = 0;
        containHelp = false;
        args = new ArrayList<>();
        argsPos = new ArrayList<>();
    }

    private void init() throws CommandSyntaxException {
        int c = getCursor();
        skipWhitespace();
        args = new ArrayList<>();
        argsPos = new ArrayList<>();
        while (canRead()) {
            StringBuilder builder = new StringBuilder();
            argsPos.add(getCursor());
            while (canRead()) {
                if (StringReader.isQuotedStringStart(peek())) {
                    builder.append(readQuotedString());
                } else if(!Character.isWhitespace(peek())) {
                    builder.append(peek());
                    skip();
                } else {
                    break;
                }
            }
            String arg = builder.toString();
//            if (arg.startsWith("--")) { //$NON-NLS-1$
                final int eq = arg.indexOf('=');
                if (eq > 0) {
                    args.add(arg.substring(0, eq));
                    args.add(arg.substring(eq + 1));
                    argsPos.add(getCursor() + eq + 1);
                    skipWhitespace();
                    continue;
                }
//            }
            args.add(arg);
            skipWhitespace();
        }
        length = args.size();
        containHelp = TextBuiltin.containsHelp(this);
        pos = 0;
        setCursor(c);
    }

    /**Find which argument current cursor at
     *
     * @return int Argument index, -1 when not found
     */
    private int currentAt() {
        int i = 0;
        while (i < length && argsPos.get(i) <= getCursor()) {
            i += 1;
        }
        return i-1-pos;
    }

    /**Get argument string at given arg index
     *
     * This method don't change cursor
     *
     * @param index the index of arg
     * @return String
     */
    public String getArg(int index) {
        index += pos;
        if (index < 0 || index >= length) return null;
        return args.get(index);
    }

    /**Get argument string at given arg index
     *
     * This method will change cursor to the argument start position
     *
     * @param index the index of arg
     * @return String
     */
    public String readArg(int index) {
        index += pos;
        if (index < 0 || index >= length) return null;
        setCursor(argsPos.get(index));
        return args.get(index);
    }

    /**Get next argument string
     *
     * This method don't change cursor
     *
     * @return String
     */
    public String getNext() {
        int i = currentAt() + 1 + pos;
        if (i>=0 && i<length) {
            return getArg(i);
        }
        return null;
    }

    /**Get next argument string
     *
     * This method don't change cursor
     *
     * @return String
     */
    public String readNext() {
        int i =currentAt() + 1 + pos;
        if (i>=0 && i<length) {
            return readArg(i);
        }
        return null;
    }

    /**Get current argument string
     *
     * This method will change cursor to the argument start position
     *
     * @return String
     */
    public String readCurrent() {
        int i = currentAt() + pos;
        if (i>=0 && i<length) {
            return readArg(i);
        }
        return null;
    }

    /**Get current argument string
     *
     * This method don't change cursor
     *
     * @return String
     */
    public String getCurrent() {
        int i = currentAt() + pos;
        if (i>=0 && i<length) {
            return getArg(i);
        }
        return null;
    }

    public int getLength() {
        return length;
    }

    public void skipArg() {
        skipArg(1);
    }
    public void skipArg(int len) {
        pos += len;
//        if (pos >= length) {
//            pos = length-1;
//        }
        if (pos < length) {
            setCursor(argsPos.get(pos));
        }
    }

    /**Set cursor to argument start position
     *
     */
    public void returnStart() {
        setCursor(argsPos.get(0));
    }
}
