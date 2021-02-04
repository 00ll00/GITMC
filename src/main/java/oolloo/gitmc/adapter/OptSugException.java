package oolloo.gitmc.adapter;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;
import org.kohsuke.args4j.NamedOptionDef;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;

public class OptSugException extends SugException{
    public OptSugException(String message) {
        super(message);
    }

    public OptSugException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptSugException(Throwable cause) {
        super(cause);
    }

    public OptSugException(CmdLineParser parser, Localizable message, String... args) {
        super(parser, message, args);
    }

    public OptSugException(CmdLineParser parser, String message) {
        super(parser, message);
    }

    public OptSugException(CmdLineParser parser, String message, Throwable cause) {
        super(parser, message, cause);
    }

    public OptSugException(CmdLineParser parser, Throwable cause) {
        super(parser, cause);
    }

    @Override
    public void suggeste(SuggestionsBuilder builder) {
        CmdLineParser parser = getParser();
        for (OptionHandler o : parser.getOptions()) {
            OptionDef od = o.option;
            if (od instanceof NamedOptionDef) {
                NamedOptionDef nod = (NamedOptionDef) od;
                builder.suggest(nod.name());
                for (String a : nod.aliases()) {
                    builder.suggest(a);
                }
            }
        }
    }


}