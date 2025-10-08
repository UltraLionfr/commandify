package fr.ultralion.commandify.events;

import fr.ultralion.commandify.Commandify;
import fr.ultralion.commandify.util.IgnoreManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;

@EventBusSubscriber(modid = Commandify.MOD_ID)
public class ChatFilter {

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayer sender = event.getPlayer();
        var server = sender.getServer();

        String plain = event.getMessage().getString();

        event.setCanceled(true);

        for (ServerPlayer target : server.getPlayerList().getPlayers()) {
            if (!IgnoreManager.isIgnoring(target, sender)) {
                target.sendSystemMessage(Component.literal("<" + sender.getName().getString() + "> " + plain));
            }
        }
    }
}
