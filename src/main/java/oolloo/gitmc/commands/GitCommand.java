package oolloo.gitmc.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import oolloo.gitmc.GitHandler;
import oolloo.gitmc.CmdResponse;
import oolloo.gitmc.GITMC;

import javax.annotation.Nullable;

public class GitCommand {
    private static final GitHandler gitHandler = GITMC.getGitHandler();

    private static final SuggestionProvider<CommandSource> repoProvider = (Context, Builder) -> ISuggestionProvider.suggest(gitHandler.getReposKey(), Builder);
    private static final SuggestionProvider<CommandSource> branchProvider = (Context, Builder) -> ISuggestionProvider.suggest(gitHandler.getBranches(), Builder);

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("git").requires((Context) -> Context.hasPermissionLevel(4))
                .then(Commands.literal("bind").executes((Context) -> gitGetBind(Context.getSource()))
                        .then(Commands.argument("repo", StringArgumentType.greedyString()).suggests(repoProvider).executes((Context) -> gitBind(Context.getSource(), StringArgumentType.getString(Context,"repo"))))
                )
                .then(Commands.literal("pull").executes((Context) -> gitPull(Context.getSource())))
                .then(Commands.literal("fetch").executes((Context) -> gitFetch(Context.getSource())))
                .then(Commands.literal("help").executes((Context) -> gitHelp(Context.getSource())))
                .then(Commands.literal("search").executes((Context) -> gitSearch(Context.getSource())))
                .then(Commands.literal("status").executes((Context) -> gitStatus(Context.getSource())))
                .then(Commands.literal("branch").executes((Context) -> gitBranch(Context.getSource(),"get_now",null))
                        .then(Commands.argument("branch", StringArgumentType.string()).suggests(branchProvider).executes((Context) -> gitBranch(Context.getSource(),"set_branch", StringArgumentType.getString(Context,"branch"))))
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("file pattern", StringArgumentType.greedyString()).executes((Context) -> gitAdd(Context.getSource(), StringArgumentType.getString(Context,"file pattern"))))
                )
                .then(Commands.literal("commit")
                        .then(Commands.argument("message", StringArgumentType.greedyString()).executes((Context) -> gitCommit(Context.getSource(), StringArgumentType.getString(Context,"message"))))
                )
                .then(Commands.literal("checkout")
                        .then(Commands.argument("branch", StringArgumentType.string()).suggests(branchProvider).executes((Context) -> gitCheckout(Context.getSource(),StringArgumentType.getString(Context,"branch"),null))
                                .then(Commands.argument("path",StringArgumentType.greedyString()).executes((Context) -> gitCheckout(Context.getSource(),StringArgumentType.getString(Context,"branch"),StringArgumentType.getString(Context,"path"))))
                        )
                )
                .then(Commands.literal("push").executes((Context) -> gitPush(Context.getSource())))
        );
    }

    public static int gitHelp(CommandSource source){
        source.sendFeedback(new StringTextComponent("version 0.3"),false);
        return 1;
    }
    public static int gitSearch(CommandSource source){
        CmdResponse response = gitHandler.search();
        source.sendFeedback(response.getComponent(),false);
        return response.getValue();
    }
    public static int gitBind(CommandSource source,String repoKey){
        CmdResponse response = gitHandler.bind(repoKey);
        if (response.isSucceed()){
            source.sendFeedback(response.getComponent(),true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitGetBind(CommandSource source){
        CmdResponse response = gitHandler.getBind();
        if(response.isSucceed()){
            source.sendFeedback(response.getComponent(),false);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitPull(CommandSource source) {
        CmdResponse response = gitHandler.pull(source);
        if(response.isSucceed()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitStatus(CommandSource source){
        CmdResponse response = gitHandler.status();
        if(response.isFinished()) {
            source.sendFeedback(response.getComponent(), false);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitBranch(CommandSource source, String action, @Nullable String argument){
        CmdResponse response = gitHandler.branch(argument);
        if(response.isFinished()) {
            source.sendFeedback(response.getComponent(), false);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitFetch(CommandSource source){
        CmdResponse response = gitHandler.fetch(source);
        if(response.isSucceed()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitAdd(CommandSource source,String filePattern){
        CmdResponse response = gitHandler.add(filePattern);
        if(response.isFinished()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitCommit(CommandSource source,String message){
        CmdResponse response = gitHandler.commit(message);
        if(response.isSucceed()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitCheckout(CommandSource source,String branch,@Nullable String path){
        CmdResponse response = gitHandler.checkout(source, branch, path);
        if(response.isSucceed()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
    public static int gitPush(CommandSource source){
        CmdResponse response = gitHandler.push(source);
        if(response.isSucceed()) {
            source.sendFeedback(response.getComponent(), true);
        }else {
            source.sendErrorMessage(response.getComponent());
        }
        return response.getValue();
    }
}