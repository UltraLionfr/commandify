package fr.ultralion.commandify.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import fr.ultralion.commandify.Commandify;
import fr.ultralion.commandify.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Lang {

    private static final Gson GSON = new Gson();
    private static final Map<String, String> TRANSLATIONS = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static String currentLang = null;

    public static void load(MinecraftServer server) {
        String langCode = Config.DEFAULT_LANGUAGE.get();
        if (langCode.equals(currentLang)) return;

        TRANSLATIONS.clear();
        currentLang = langCode;

        File langDir = new File("config/commandify/lang");
        if (!langDir.exists()) {
            langDir.mkdirs();
            System.out.println(ConsoleColors.CYAN + "[Commandify] Created language folder: " + langDir.getAbsolutePath() + ConsoleColors.RESET);
        }

        copyAllLangFiles();

        File langFile = new File(langDir, langCode + ".json");
        if (!langFile.exists()) {
            System.out.println(ConsoleColors.YELLOW + "[Commandify] Language file '" + langCode + "' not found in config folder, falling back to en_us.json" + ConsoleColors.RESET);
            langFile = new File(langDir, "en_us.json");
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(langFile), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            TRANSLATIONS.putAll(GSON.fromJson(reader, type));
            System.out.println(ConsoleColors.GREEN + "[Commandify] Loaded language: " + langCode + " (" + TRANSLATIONS.size() + " entries)" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to load language " + langCode + ": " + e.getMessage() + ConsoleColors.RESET);
            loadFallback();
        }
    }

    private static void copyAllLangFiles() {
        String[] availableLangs = {"en_us", "fr_fr"};

        for (String lang : availableLangs) {
            File target = new File("config/commandify/lang/" + lang + ".json");
            if (!target.exists()) {
                copyFromJar(lang);
            }
        }
    }

    private static void copyFromJar(String langCode) {
        String internalPath = "/assets/" + Commandify.MOD_ID + "/lang/" + langCode + ".json";
        File target = new File("config/commandify/lang/" + langCode + ".json");

        try (InputStream in = Lang.class.getResourceAsStream(internalPath)) {
            if (in == null) {
                System.out.println(ConsoleColors.YELLOW + "[Commandify] Could not find language '" + langCode + "' inside assets, skipping copy." + ConsoleColors.RESET);
                return;
            }
            Files.copy(in, target.toPath());
            System.out.println(ConsoleColors.GREEN + "[Commandify] Copied language '" + langCode + "' to config/commandify/lang/" + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to copy language '" + langCode + "': " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private static void loadFallback() {
        try (InputStream in = Lang.class.getResourceAsStream("/assets/" + Commandify.MOD_ID + "/lang/en_us.json")) {
            if (in == null) {
                System.out.println(ConsoleColors.RED + "[Commandify] Could not load fallback language en_us.json!" + ConsoleColors.RESET);
                return;
            }
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            TRANSLATIONS.putAll(GSON.fromJson(new InputStreamReader(in), type));
            System.out.println(ConsoleColors.YELLOW + "[Commandify] Fallback language 'en_us' loaded (" + TRANSLATIONS.size() + " entries)" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to load fallback language: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    public static Component t(String key, Object... args) {
        String raw = TRANSLATIONS.getOrDefault(key, key);
        try {
            raw = String.format(raw, args);
        } catch (Exception ignored) {
        }
        return Component.literal(raw);
    }

    public static Component raw(String key) {
        return Component.literal(TRANSLATIONS.getOrDefault(key, key));
    }

    public static String getRaw(String key) {
        return TRANSLATIONS.getOrDefault(key, key);
    }

    /**
     * Console colors for pretty output (ANSI-compatible terminals)
     */
    public static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";
        public static final String BOLD = "\u001B[1m";
    }
}
