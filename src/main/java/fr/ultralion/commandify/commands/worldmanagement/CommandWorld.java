package fr.ultralion.commandify.commands.worldmanagement;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;

public class CommandWorld {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        //day
        dispatcher.register(
                Commands.literal("day")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_DAY_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setDayTime(1000);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.day"));
                            return 1;
                        })
        );

        //night
        dispatcher.register(
                Commands.literal("night")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_NIGHT_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setDayTime(13000);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.night"));
                            return 1;
                        })
        );

        //noon
        dispatcher.register(
                Commands.literal("noon")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_NOON_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setDayTime(6000);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.noon"));
                            return 1;
                        })
        );

        //midnight
        dispatcher.register(
                Commands.literal("midnight")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_MIDNIGHT_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setDayTime(18000);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.midnight"));
                            return 1;
                        })
        );

        //sun
        dispatcher.register(
                Commands.literal("sun")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_SUN_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setWeatherParameters(6000, 0, false, false);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.sun"));
                            return 1;
                        })
        );

        //rain
        dispatcher.register(
                Commands.literal("rain")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_RAIN_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setWeatherParameters(0, 6000, true, false);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.rain"));
                            return 1;
                        })
        );

        //thunder
        dispatcher.register(
                Commands.literal("thunder")
                        .requires(src -> src.hasPermission(2))
                        .executes(ctx -> {
                            if (!Config.ENABLE_THUNDER_COMMAND.get()) return disabled(ctx.getSource());
                            ServerLevel world = ctx.getSource().getLevel();
                            world.setWeatherParameters(0, 6000, true, true);
                            ctx.getSource().sendSystemMessage(Lang.t("commandify.world.thunder"));
                            return 1;
                        })
        );
    }

    private static int disabled(CommandSourceStack source) {
        source.sendSystemMessage(Lang.t("commandify.world.disabled"));
        return 0;
    }
}
