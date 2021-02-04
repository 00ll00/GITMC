package oolloo.gitmc.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.adapter.SugException;
import org.eclipse.jgit.pgm.GitHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class GitCLArgument implements ArgumentType<GitHandler> {
    private final DynamicCommandExceptionType PARSE_FATAL = new DynamicCommandExceptionType((msg) -> new StringTextComponent((String) msg));

    public int help(CommandContext context) {
        try {
            new GitHandler().help((CommandSource) context.getSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int run(CommandContext context) {
        try {
            GitHandler handler = (GitHandler) context.getArgument("git-command-line", GitHandler.class);
            handler.run((CommandSource) context.getSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public GitHandler parse(StringReader reader) throws CommandSyntaxException {
        try {
            return new GitHandler().parse(readArgs(reader));
        } catch (Exception e) {
            throw PARSE_FATAL.create(e.getMessage());
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof ISuggestionProvider) {
            try {
                StringReader reader = new StringReader(builder.getInput());
                reader.setCursor(builder.getStart());
                new GitHandler().parse(readArgs(reader));
            } catch (SugException e) {
                e.suggeste(builder);
            } catch (Exception ignore) {
            }
//            builder.createOffset()
            return builder.buildFuture();
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return null;
    }

    private String[] readArgs(StringReader reader) throws CommandSyntaxException {
        reader.skipWhitespace();
        ArrayList<String> args = new ArrayList<>();
        while (reader.canRead()) {
            StringBuilder builder = new StringBuilder();
            while (reader.canRead()) {
                if (StringReader.isQuotedStringStart(reader.peek())) {
                    builder.append(reader.readQuotedString());
                } else if(!Character.isWhitespace(reader.peek())) {
                    builder.append(reader.peek());
                    reader.skip();
                } else {
                    break;
                }
            }
            args.add(builder.toString());
            reader.skipWhitespace();
        }
        return args.toArray(new String[0]);
    }
}
