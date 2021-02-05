package oolloo.gitmc.adapter;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.codehaus.plexus.util.cli.Arg;
import org.eclipse.jgit.pgm.CommandCatalog;
import org.eclipse.jgit.pgm.CommandRef;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;

public class SubCmdSugException extends SugException{
    public SubCmdSugException(String message) {
        super(message);
    }

    public SubCmdSugException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubCmdSugException(Throwable cause) {
        super(cause);
    }

    public SubCmdSugException(CmdLineParser parser, Localizable message, String... args) {
        super(parser, message, args);
    }

    public SubCmdSugException(CmdLineParser parser, String message) {
        super(parser, message);
    }

    public SubCmdSugException(CmdLineParser parser, String message, Throwable cause) {
        super(parser, message, cause);
    }

    public SubCmdSugException(CmdLineParser parser, Throwable cause) {
        super(parser, cause);
    }

    @Override
    public SuggestionsBuilder suggeste(SuggestionsBuilder builder) {
        builder = builder.createOffset (((ArgReader) getParser().cmdLine).getCursor());
        for (CommandRef cr : CommandCatalog.all()) {
            builder.suggest(cr.getName());
        }
        return builder;
    }
}
