package oolloo.gitmc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oolloo.gitmc.command.GitCommand;
import oolloo.gitmc.command.SshCommand;
import oolloo.gitmc.old.GitHandler;
import oolloo.gitmc.old.SshHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("gitmc")
public class GITMC
{
    public static final String CONFIG_PATH = "config/gitmc/";
    public static final String SSH_PATH = CONFIG_PATH+".ssh/";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private static final GitHandler gitHandler = new GitHandler();
    private static final SshHandler sshHandler = new SshHandler();

    public GITMC() {
        MinecraftForge.EVENT_BUS.register(this);
        CheckFiles();
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event)
    {
        CommandDispatcher<net.minecraft.command.CommandSource> dispatcher = event.getDispatcher();
        GitCommand.register(dispatcher);
        SshCommand.register(dispatcher);
    }

    public static GitHandler getGitHandler(){
        return gitHandler;
    }
    public static SshHandler getSshHandler(){
        return sshHandler;
    }

    public static void CheckFiles(){
        File file;
        file = new File(CONFIG_PATH);
        if(!file.exists()) file.mkdir();
        file = new File(SSH_PATH);
        if(!file.exists()) file.mkdir();
    }
}
