package oolloo.gitmc.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import oolloo.gitmc.command.arguments.GitCLArgument;
import oolloo.gitmc.gitcli.GitHandler;

public class GitCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("git").requires((context) -> context.hasPermissionLevel(4))
            .then(Commands.argument("git-command-line", new GitCLArgument())).executes((context) -> GitHandler.run(GitCLArgument.getRunner(context,"git-command-line")))
        );
    }
}