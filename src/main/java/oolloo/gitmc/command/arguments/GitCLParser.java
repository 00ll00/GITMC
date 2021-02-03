package oolloo.gitmc.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.gitcli.GitRunner;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GitCLParser {

    private static final SimpleCommandExceptionType NO_ARG_VALUE = new SimpleCommandExceptionType(new StringTextComponent("Argument value expected."));

    private final ArrayList<String> suggestions;
    private GitRunner runner;
    private final StringReader reader;


    public GitCLParser(StringReader readerIn) {
        this.reader = readerIn;
        this.suggestions = new ArrayList<>();
    }

    public void parse () throws CommandSyntaxException {
        runner = new GitRunner();
        suggestions.clear();

        while (reader.canRead()) {
            String str = reader.readString();
            if (str.equals("--")) {
                break;
            }

            if (str.startsWith("--")) {
                final int eq = str.indexOf('=');
                if (eq > 0) {
                    runner.setArgument(str.substring(0, eq),str.substring(eq + 1));
                    continue;
                } else {
                    if (runner.hasArg())
                    if (reader.canRead()) {
                        runner.setArgument(str, reader.readString());
                    } else {
                        throw NO_ARG_VALUE.create();
                    }
                }
            }

            if (str.startsWith("-")) {
                runner.setOption(str);
            }
        }
    }

    public GitRunner getRunner() {
        return runner;
    }


    public CompletableFuture<Suggestions> fillSuggestion(SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(suggestions,builder);
    }
}
