package com.yomi.nowtime.util;

import com.yomi.nowtime.config.ConfigManager;
import net.minecraft.server.MinecraftServer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeProvider {
    private static final DateTimeFormatter LOCAL_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String getFormattedTime(MinecraftServer server) {
        if (ConfigManager.config.useLocalTime) {
            return LocalDateTime.now().format(LOCAL_FORMATTER);
        }
        return getGameTime(server);
    }

    private static String getGameTime(MinecraftServer server) {
        long worldTime = server.getOverworld().getTimeOfDay();
        long hours = (worldTime / 1000 + 6) % 24;
        long minutes = (worldTime % 1000) * 60 / 1000;
        return String.format("%02d:%02d", hours, minutes);
    }
}