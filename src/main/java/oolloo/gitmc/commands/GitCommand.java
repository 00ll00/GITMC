package oolloo.gitmc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.GITMC;

import javax.annotation.Nullable;

public class GitCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("git").requires((Context) -> Context.hasPermissionLevel(4))
                .then(Commands.argument("git-command-line",new GitCLArgument()).executes((context -> new GitCLArgument().run(context))))
                .executes((context -> new GitCLArgument().help(context)))
        );
    }
}