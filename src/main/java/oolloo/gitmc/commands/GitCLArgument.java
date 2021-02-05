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
import oolloo.gitmc.adapter.ArgReader;
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
            ArgReader argReader = new ArgReader(reader);
            reader.setCursor(reader.getTotalLength());
            return new GitHandler().parse(argReader);
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
                new GitHandler().parse(new ArgReader(reader));
            } catch (SugException e) {
                return e.suggeste(builder).buildFuture();
            } catch (Exception ignore) {
            }
        }
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return null;
    }
}
