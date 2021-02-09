package oolloo.gitmc.adapter;

import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;

public class OptSugException extends SugException{
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
    public SuggestionsBuilder suggest(SuggestionsBuilder builder) {
        CmdLineParser parser = getParser();

        return null;
    }
}
