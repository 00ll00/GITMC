package oolloo.gitmc.adapter;

import net.minecraft.command.CommandSource;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import org.eclipse.jgit.util.io.ThrowingPrintWriter;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class Writer extends java.io.Writer {
    private final CommandSource source;
    private IFormattableTextComponent buffer;
    private boolean isErrWriter;
    private Style defaultStyle;

    public Writer (CommandSource sourceIn, boolean isErrWriterIn) {
        source = sourceIn;
        buffer = new StringTextComponent("");
        isErrWriter = isErrWriterIn;
        defaultStyle = Style.field_240709_b_;
    }
    public Writer (CommandSource sourceIn) {
        this(sourceIn,false);
    }

    public Writer append (String str) {
        str = str.replace("\t","    ");
        buffer.func_230529_a_(new StringTextComponent(str).func_230530_a_(defaultStyle));
        return this;
    }
    public Writer append (String str, Style style) {
        str = str.replace("\t","    ");
        buffer.func_230529_a_(new StringTextComponent(str).func_230530_a_(style));
        return this;
    }

    public void print(Object obj) {
        append(String.valueOf(obj));
    }

    public void println() {
        flush();
    }

    public void println(String str) {
        append(str);
        flush();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        append(String.valueOf(cbuf));
    }

    public void flush () {
        if (!buffer.getString().equals("")) {
            if (isErrWriter) {
                source.sendErrorMessage(buffer);
            } else {
                source.sendFeedback(buffer, true);
            }
        }
        buffer = new StringTextComponent("");
    }

    @Override
    public void close() throws IOException {
        flush();
    }

    public void format(String fmt, Object... args ) {
        print(String.format(fmt, args));
    }
}
