package oolloo.gitmc.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import oolloo.gitmc.old.CmdResponse;
import oolloo.gitmc.GITMC;
import oolloo.gitmc.old.SshHandler;

public class SshCommand {
    private static final SshHandler sshHandler = GITMC.getSshHandler();
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("ssh").requires((Context) -> Context.hasPermissionLevel(4))
                .then(Commands.literal("keygen")
                        .then(Commands.argument("comment", StringArgumentType.string()).executes((Context) -> sshKeyGen(Context.getSource(), StringArgumentType.getString(Context,"comment"),false))
                                .then(Commands.literal("replace").executes((Context) -> sshKeyGen(Context.getSource(), StringArgumentType.getString(Context,"comment"),true)))
                                .then(Commands.literal("keep").executes((Context) -> sshKeyGen(Context.getSource(), StringArgumentType.getString(Context,"comment"),false)))
                        )
                )
                .then(Commands.literal("check").executes((Context) -> sshCheck(Context.getSource())))
        );
    }
    public static int sshKeyGen(CommandSource source,String comment,boolean replace){
        CmdResponse response = sshHandler.keyGen(comment,replace);
        if(response.isSucceed()){
            source.sendFeedback(response.getComponent(),false);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int sshCheck(CommandSource source){
        CmdResponse response = sshHandler.check();
        if(response.isFinished()){
            source.sendFeedback(response.getComponent(),false);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
}
