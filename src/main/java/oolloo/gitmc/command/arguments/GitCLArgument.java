package oolloo.gitmc.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import oolloo.gitmc.gitcli.GitRunner;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class GitCLArgument implements ArgumentType<GitRunner> {

    @Override
    public GitRunner parse(StringReader reader) throws CommandSyntaxException {
        GitCLParser parser = new GitCLParser(reader);
        parser.parse();
        return parser.getRunner();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof ISuggestionProvider) {
            StringReader stringreader = new StringReader(context.getInput());
            stringreader.setCursor(builder.getStart());
            GitCLParser parser = new GitCLParser(stringreader);

            try {
                parser.parse();
            } catch (CommandSyntaxException ignored) {
            }

            return parser.fillSuggestion(builder);
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return null;
    }

    public static GitRunner getRunner(final CommandContext<?> context, final String name) {
        return context.getArgument(name,GitRunner.class);
    }
}
