package oolloo.gitmc.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.FileHelper;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class FileArgumentType implements ArgumentType<List<File>> {
    private static final Collection<String> EXAMPLES = Arrays.asList("test.txt", "./123/a.png", "*.json");
    public static final SimpleCommandExceptionType NO_FILE_FOUND = new SimpleCommandExceptionType(new StringTextComponent("No such file."));
    public static final SimpleCommandExceptionType SINGLE_FILE_ONLY = new SimpleCommandExceptionType(new StringTextComponent("Single file only, but multiple files found."));
    private final boolean single;
    private final boolean fileOnly;
    private final boolean dirOnly;
    private final boolean isNew;

    protected FileArgumentType(boolean singleIn, boolean fileOnlyIn, boolean dirOnlyIn, boolean isNewIn) {
        this.single = singleIn;
        this.fileOnly = fileOnlyIn;
        this.dirOnly = dirOnlyIn;
        this.isNew = isNewIn;
    }

    public List<File> parse(StringReader reader) throws CommandSyntaxException {
        String strIn = reader.readString();

        String path = Pattern.compile(".*/").matcher(strIn).group();
        String name = Pattern.compile("[^/]+$").matcher(strIn).group();

        List<File> res = FileHelper.findThisDir(path,name,dirOnly);
        if (res.isEmpty()) {
            throw NO_FILE_FOUND.create();
        }
        if (res.size() > 1 && single) {
            throw SINGLE_FILE_ONLY.create();
        }
        return FileHelper.findThisDir(path,name,dirOnly);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        return null;
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
