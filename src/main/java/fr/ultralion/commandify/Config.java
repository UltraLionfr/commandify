package fr.ultralion.commandify;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final ModConfigSpec.BooleanValue ENABLE_SETSPAWN_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_RELOAD_COMMAND;
    public static final ModConfigSpec.ConfigValue<String> DEFAULT_LANGUAGE;
    public static final ModConfigSpec GENERAL_SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_SPAWN_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_TPA;
    public static final ModConfigSpec.BooleanValue ENABLE_TPAHERE;
    public static final ModConfigSpec.BooleanValue ENABLE_TPAACCEPT;
    public static final ModConfigSpec.BooleanValue ENABLE_TPADENY;
    public static final ModConfigSpec.BooleanValue ENABLE_TPATOGGLE;
    public static final ModConfigSpec.BooleanValue ENABLE_HOME_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_SETHOME_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_DELHOME_COMMAND;
    public static final ModConfigSpec.IntValue MAX_HOMES;
    public static final ModConfigSpec.BooleanValue ENABLE_WARP_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_WARPS_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_SETWARP_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_DELWARP_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_RTP_COMMAND;
    public static final ModConfigSpec.IntValue RTP_MAX_DISTANCE;
    public static final ModConfigSpec.IntValue RTP_COOLDOWN;
    public static final ModConfigSpec.BooleanValue ENABLE_BACK_COMMAND;
    public static final ModConfigSpec TELEPORT_SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_DAY_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_NIGHT_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_NOON_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_MIDNIGHT_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_SUN_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_RAIN_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_THUNDER_COMMAND;
    public static final ModConfigSpec WORLD_SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_MSG_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_REPLY_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_BROADCAST_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_IGNORE_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_IGNORED_LIST_COMMAND;
    public static final ModConfigSpec.BooleanValue ENABLE_IGNORE_IN_GLOBAL_CHAT;
    public static final ModConfigSpec CHAT_SPEC;
    public static final ModConfigSpec.BooleanValue ENABLE_INVSEE_COMMAND;
    public static final ModConfigSpec ADMIN_SPEC;
    // GENERAL
    private static final ModConfigSpec.Builder GENERAL_BUILDER = new ModConfigSpec.Builder();
    // TELEPORTATION
    private static final ModConfigSpec.Builder TELEPORT_BUILDER = new ModConfigSpec.Builder();
    // WORLD MANAGEMENT
    private static final ModConfigSpec.Builder WORLD_BUILDER = new ModConfigSpec.Builder();
    // CHAT COMMANDS
    private static final ModConfigSpec.Builder CHAT_BUILDER = new ModConfigSpec.Builder();
    // ADMIN / MODERATION COMMANDS
    private static final ModConfigSpec.Builder ADMIN_BUILDER = new ModConfigSpec.Builder();

    static {
        // GENERAL
        GENERAL_BUILDER.comment("General configuration for Commandify").push("general");
        ENABLE_SETSPAWN_COMMAND = GENERAL_BUILDER.comment("Enable /setspawn").define("enableSetSpawnCommand", true);
        ENABLE_RELOAD_COMMAND = GENERAL_BUILDER.comment("Enable /commandify reload").define("enableReloadCommand", true);

        DEFAULT_LANGUAGE = GENERAL_BUILDER
                .comment("Default language for Commandify (e.g., 'en_us' or 'fr_fr')")
                .define("defaultLanguage", "en_us");

        GENERAL_BUILDER.pop();
        GENERAL_SPEC = GENERAL_BUILDER.build();


        // TELEPORTATION
        TELEPORT_BUILDER.comment("Teleportation, homes, warps, and RTP").push("teleportation");
        ENABLE_SPAWN_COMMAND = TELEPORT_BUILDER.comment("Enable /spawn").define("enableSpawnCommand", true);
        ENABLE_TPA = TELEPORT_BUILDER.comment("Enable /tpa").define("enableTpa", true);
        ENABLE_TPAHERE = TELEPORT_BUILDER.comment("Enable /tpahere").define("enableTpahere", true);
        ENABLE_TPAACCEPT = TELEPORT_BUILDER.comment("Enable /tpaccept").define("enableTpaccept", true);
        ENABLE_TPADENY = TELEPORT_BUILDER.comment("Enable /tpadeny").define("enableTpadeny", true);
        ENABLE_TPATOGGLE = TELEPORT_BUILDER.comment("Enable /tpatoggle").define("enableTpatoggle", true);
        ENABLE_HOME_COMMAND = TELEPORT_BUILDER.comment("Enable /home").define("enableHomeCommand", true);
        ENABLE_SETHOME_COMMAND = TELEPORT_BUILDER.comment("Enable /sethome").define("enableSetHomeCommand", true);
        ENABLE_DELHOME_COMMAND = TELEPORT_BUILDER.comment("Enable /delhome").define("enableDelHomeCommand", true);
        MAX_HOMES = TELEPORT_BUILDER.comment("Maximum number of homes per player").defineInRange("maxHomes", 3, 1, 100);
        ENABLE_WARP_COMMAND = TELEPORT_BUILDER.comment("Enable /warp").define("enableWarpCommand", true);
        ENABLE_WARPS_COMMAND = TELEPORT_BUILDER.comment("Enable /warps").define("enableWarpsCommand", true);
        ENABLE_SETWARP_COMMAND = TELEPORT_BUILDER.comment("Enable /setwarp").define("enableSetWarpCommand", true);
        ENABLE_DELWARP_COMMAND = TELEPORT_BUILDER.comment("Enable /delwarp").define("enableDelWarpCommand", true);
        ENABLE_RTP_COMMAND = TELEPORT_BUILDER.comment("Enable /rtp (random teleport)").define("enableRtpCommand", true);
        RTP_MAX_DISTANCE = TELEPORT_BUILDER.comment("Maximum RTP distance (in blocks)").defineInRange("rtpMaxDistance", 30000, 5000, 100000);
        RTP_COOLDOWN = TELEPORT_BUILDER.comment("RTP cooldown (in seconds)").defineInRange("rtpCooldown", 60, 0, 3600);
        ENABLE_BACK_COMMAND = TELEPORT_BUILDER.comment("Enable /back").define("enableBackCommand", true);
        TELEPORT_BUILDER.pop();
        TELEPORT_SPEC = TELEPORT_BUILDER.build();


        // WORLD MANAGEMENT
        WORLD_BUILDER.comment("World management (time, weather, etc.)").push("world");
        ENABLE_DAY_COMMAND = WORLD_BUILDER.comment("Enable /day").define("enableDayCommand", true);
        ENABLE_NIGHT_COMMAND = WORLD_BUILDER.comment("Enable /night").define("enableNightCommand", true);
        ENABLE_NOON_COMMAND = WORLD_BUILDER.comment("Enable /noon").define("enableNoonCommand", true);
        ENABLE_MIDNIGHT_COMMAND = WORLD_BUILDER.comment("Enable /midnight").define("enableMidnightCommand", true);
        ENABLE_SUN_COMMAND = WORLD_BUILDER.comment("Enable /sun").define("enableSunCommand", true);
        ENABLE_RAIN_COMMAND = WORLD_BUILDER.comment("Enable /rain").define("enableRainCommand", true);
        ENABLE_THUNDER_COMMAND = WORLD_BUILDER.comment("Enable /thunder").define("enableThunderCommand", true);
        WORLD_BUILDER.pop();
        WORLD_SPEC = WORLD_BUILDER.build();


        // CHAT
        CHAT_BUILDER.comment("Private chat commands, broadcast, and ignore system").push("chat");
        ENABLE_MSG_COMMAND = CHAT_BUILDER.comment("Enable /msg (private messages)").define("enableMsgCommand", true);
        ENABLE_REPLY_COMMAND = CHAT_BUILDER.comment("Enable /r (reply to last private message)").define("enableReplyCommand", true);
        ENABLE_BROADCAST_COMMAND = CHAT_BUILDER.comment("Enable /broadcast (admin global message)").define("enableBroadcastCommand", true);
        ENABLE_IGNORE_COMMAND = CHAT_BUILDER.comment("Enable /ignore and /unignore (block private messages)").define("enableIgnoreCommand", true);
        ENABLE_IGNORED_LIST_COMMAND = CHAT_BUILDER.comment("Enable /ignored (view ignored players list)").define("enableIgnoredListCommand", true);
        ENABLE_IGNORE_IN_GLOBAL_CHAT = CHAT_BUILDER.comment("Hide ignored players' messages in global chat").define("ignoreInGlobalChat", true);
        CHAT_BUILDER.pop();
        CHAT_SPEC = CHAT_BUILDER.build();


        // ADMIN / MODERATION
        ADMIN_BUILDER.comment("Administrator and moderation commands").push("admin");
        ENABLE_INVSEE_COMMAND = ADMIN_BUILDER.comment("Enable /invsee (view and edit a player's inventory)").define("enableInvseeCommand", true);
        ADMIN_BUILDER.pop();
        ADMIN_SPEC = ADMIN_BUILDER.build();
    }


    // Register config files
    public static void register() {
        var context = ModLoadingContext.get().getActiveContainer();
        context.registerConfig(ModConfig.Type.SERVER, GENERAL_SPEC, "commandify/commandify-general.toml");
        context.registerConfig(ModConfig.Type.SERVER, TELEPORT_SPEC, "commandify/teleportation.toml");
        context.registerConfig(ModConfig.Type.SERVER, WORLD_SPEC, "commandify/world.toml");
        context.registerConfig(ModConfig.Type.SERVER, CHAT_SPEC, "commandify/chat.toml");
        context.registerConfig(ModConfig.Type.SERVER, ADMIN_SPEC, "commandify/admin.toml");
    }
}
