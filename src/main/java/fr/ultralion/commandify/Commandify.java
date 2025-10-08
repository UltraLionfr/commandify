package fr.ultralion.commandify;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.commands.admin.CommandInvsee;
import fr.ultralion.commandify.commands.chat.*;
import fr.ultralion.commandify.commands.other.CommandCommandifyInfo;
import fr.ultralion.commandify.commands.reload.ReloadCommand;
import fr.ultralion.commandify.commands.teleportation.back.CommandBack;
import fr.ultralion.commandify.commands.teleportation.homes.*;
import fr.ultralion.commandify.commands.teleportation.rtp.CommandRtp;
import fr.ultralion.commandify.commands.teleportation.spawn.*;
import fr.ultralion.commandify.commands.teleportation.teleport.*;
import fr.ultralion.commandify.commands.teleportation.warps.*;
import fr.ultralion.commandify.commands.worldmanagement.CommandWorld;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(Commandify.MOD_ID)
public class Commandify {

    public static final String MOD_ID = "commandify";
    public static final String MOD_VERSION = "1.0.1";

    public static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
        public static final String BOLD = "\u001B[1m";
    }

    public Commandify() {
        if (FMLLoader.getDist() != Dist.DEDICATED_SERVER) {
            System.out.println(ConsoleColors.RED + "[Commandify] This mod is for servers only!" + ConsoleColors.RESET);
            throw new IllegalStateException("[Commandify] Client execution detected.");
        }

        Config.register();
        System.out.println(ConsoleColors.GREEN + "[Commandify] Server-side mod initialized successfully!" + ConsoleColors.RESET);
    }

    @EventBusSubscriber(modid = Commandify.MOD_ID, value = Dist.DEDICATED_SERVER)
    public static class ServerEvents {

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            var server = event.getServer();

            System.out.println(ConsoleColors.CYAN + "[Commandify] Loading language files..." + ConsoleColors.RESET);
            Lang.load(server);

            System.out.println(ConsoleColors.CYAN + "[Commandify] Initializing IgnoreManager..." + ConsoleColors.RESET);
            IgnoreManager.init(server);

            System.out.println(ConsoleColors.GREEN + "[Commandify] Language loaded: " +
                    Config.DEFAULT_LANGUAGE.get() + ConsoleColors.RESET);
            System.out.println(ConsoleColors.GREEN + "[Commandify] IgnoreManager initialized successfully!" + ConsoleColors.RESET);
        }

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

            System.out.println(ConsoleColors.YELLOW + "[Commandify] Registering commands..." + ConsoleColors.RESET);

            SetSpawnCommand.register(dispatcher);
            SpawnCommand.register(dispatcher);
            ReloadCommand.register(dispatcher);

            CommandTpa.register(dispatcher);
            CommandTpahere.register(dispatcher);
            CommandTpaccept.register(dispatcher);
            CommandTpadeny.register(dispatcher);
            CommandTpatoggle.register(dispatcher);

            CommandSetHome.register(dispatcher);
            CommandHome.register(dispatcher);
            CommandDelHome.register(dispatcher);
            CommandHomes.register(dispatcher);

            CommandWarp.register(dispatcher);
            CommandSetWarp.register(dispatcher);
            CommandDelWarp.register(dispatcher);
            CommandWarps.register(dispatcher);

            CommandBack.register(dispatcher);
            CommandRtp.register(dispatcher);

            CommandWorld.register(dispatcher);

            CommandMsg.register(dispatcher);
            CommandReply.register(dispatcher);
            CommandBroadcast.register(dispatcher);
            CommandIgnore.register(dispatcher);
            CommandUnignore.register(dispatcher);
            CommandIgnored.register(dispatcher);

            CommandCommandifyInfo.register(dispatcher);
            CommandInvsee.register(dispatcher);

            System.out.println(ConsoleColors.GREEN + "[Commandify] All commands have been successfully registered!" + ConsoleColors.RESET);
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            System.out.println(ConsoleColors.YELLOW + "[Commandify] The server is stopping... cleaning up resources." + ConsoleColors.RESET);

            IgnoreManager.clearAll();

            System.out.println(ConsoleColors.GREEN + "[Commandify] Commandify has been safely disabled (all data preserved)." + ConsoleColors.RESET);
        }
    }
}
