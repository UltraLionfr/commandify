package fr.ultralion.commandify.commands.teleportation.teleport;

import fr.ultralion.commandify.util.Lang;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TeleportManager {

    private static final Map<UUID, TpaRequest> pendingRequests = new ConcurrentHashMap<>();
    private static final Set<UUID> tpaDisabled = ConcurrentHashMap.newKeySet();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int EXPIRATION_SECONDS = 60;

    public static boolean sendRequest(ServerPlayer sender, ServerPlayer target, boolean here) {
        if (tpaDisabled.contains(target.getUUID())) return false;

        UUID targetId = target.getUUID();
        TpaRequest request = new TpaRequest(sender.getUUID(), here);
        pendingRequests.put(targetId, request);

        scheduler.schedule(() -> expireRequest(target, request), EXPIRATION_SECONDS, TimeUnit.SECONDS);
        return true;
    }

    public static TpaRequest getRequest(UUID targetId) {
        return pendingRequests.get(targetId);
    }

    public static void removeRequest(UUID targetId) {
        pendingRequests.remove(targetId);
    }

    public static boolean isTpaDisabled(ServerPlayer player) {
        return tpaDisabled.contains(player.getUUID());
    }

    public static boolean toggleTpa(ServerPlayer player) {
        UUID id = player.getUUID();
        if (tpaDisabled.remove(id)) {
            return true;
        } else {
            tpaDisabled.add(id);
            return false;
        }
    }

    private static void expireRequest(ServerPlayer target, TpaRequest request) {
        TpaRequest current = pendingRequests.get(target.getUUID());
        if (current != null && current == request) {
            pendingRequests.remove(target.getUUID());

            ServerPlayer requester = target.getServer().getPlayerList().getPlayer(request.getRequester());

            if (requester != null) {
                requester.sendSystemMessage(Lang.t("commandify.tpa.expired_requester", target.getName().getString()));
            }

            target.sendSystemMessage(Lang.t(
                    "commandify.tpa.expired_target",
                    requester != null ? requester.getName().getString() : Lang.getRaw("commandify.tpa.unknown_player")
            ));
        }
    }

    public static class TpaRequest {
        private final UUID requester;
        private final boolean here;
        private final Instant created;

        public TpaRequest(UUID requester, boolean here) {
            this.requester = requester;
            this.here = here;
            this.created = Instant.now();
        }

        public UUID getRequester() {
            return requester;
        }

        public boolean isHere() {
            return here;
        }

        public Instant getCreated() {
            return created;
        }
    }
}
