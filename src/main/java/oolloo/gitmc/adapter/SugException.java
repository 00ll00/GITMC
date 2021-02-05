package oolloo.gitmc.adapter;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;

public abstract class SugException extends CmdLineException {

    public SugException(String message) {
        super(message);
    }

    public SugException(String message, Throwable cause) {
        super(message, cause);
    }

    public SugException(Throwable cause) {
        super(cause);
    }

    public SugException(CmdLineParser parser, Localizable message, String... args) {
        super(parser, message, args);
    }

    public SugException(CmdLineParser parser, String message) {
        super(parser, message);
    }

    public SugException(CmdLineParser parser, String message, Throwable cause) {
        super(parser, message, cause);
    }

    public SugException(CmdLineParser parser, Throwable cause) {
        super(parser, cause);
    }

    public abstract SuggestionsBuilder suggeste (SuggestionsBuilder builder);
}
