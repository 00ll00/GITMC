package oolloo.gitmc.adapter;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;
import org.kohsuke.args4j.NamedOptionDef;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;

public class OptNameSugException extends SugException{

    public OptNameSugException(CmdLineParser parser, Localizable message, String... args) {
        super(parser, message, args);
    }

    public OptNameSugException(CmdLineParser parser, String message) {
        super(parser, message);
    }

    public OptNameSugException(CmdLineParser parser, String message, Throwable cause) {
        super(parser, message, cause);
    }

    public OptNameSugException(CmdLineParser parser, Throwable cause) {
        super(parser, cause);
    }

    @Override
    public SuggestionsBuilder suggest(SuggestionsBuilder builder) {
        CmdLineParser parser = getParser();
        builder = builder.createOffset(((ArgReader) parser.cmdLine).getCursor());
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
        return builder;
    }


}
