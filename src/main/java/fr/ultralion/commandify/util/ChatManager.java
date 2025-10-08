package fr.ultralion.commandify.util;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager {

    private static final Map<UUID, UUID> LAST_MSG = new HashMap<>();

    public static void setLastMessaged(ServerPlayer sender, ServerPlayer target) {
        LAST_MSG.put(sender.getUUID(), target.getUUID());
        LAST_MSG.put(target.getUUID(), sender.getUUID());
    }

    public static UUID getLastMessaged(ServerPlayer player) {
        return LAST_MSG.get(player.getUUID());
    }

    public static void clear() {
        LAST_MSG.clear();
    }
}
