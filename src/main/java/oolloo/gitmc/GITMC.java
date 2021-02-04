package oolloo.gitmc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oolloo.gitmc.commands.GitCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("gitmc")
public class GITMC
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public GITMC() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event)
    {
        CommandDispatcher<net.minecraft.command.CommandSource> dispatcher = event.getDispatcher();
        GitCommand.register(dispatcher);
    }
}
