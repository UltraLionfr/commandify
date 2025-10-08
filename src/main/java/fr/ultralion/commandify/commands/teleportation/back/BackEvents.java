package fr.ultralion.commandify.commands.teleportation.back;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import fr.ultralion.commandify.Commandify;

@EventBusSubscriber(modid = Commandify.MOD_ID, value = Dist.DEDICATED_SERVER)
public class BackEvents {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BackManager.saveLastLocation(player);
        }
    }

    @SubscribeEvent
    public static void onTeleport(EntityTeleportEvent.EnderEntity event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BackManager.saveLastLocation(player);
        }
    }
}
