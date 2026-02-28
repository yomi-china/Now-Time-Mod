package com.yomi.nowtime.scheduler;

import com.yomi.nowtime.config.ConfigManager;
import com.yomi.nowtime.util.TimeProvider;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.quartz.CronExpression;
import java.text.ParseException;
import java.util.Date;

public class AnnouncementScheduler {
    private static CronExpression cron;
    private static Date nextTrigger;

    public static void initialize(MinecraftServer server) {
        try {
            cron = new CronExpression(ConfigManager.config.cronExpression);
            nextTrigger = cron.getNextValidTimeAfter(new Date());
        } catch (ParseException e) {
            server.sendMessage(Text.literal("无效的cron表达式，使用默认值"));
            try {
                cron = new CronExpression("0 */60 * * * ?");
                nextTrigger = cron.getNextValidTimeAfter(new Date());
            } catch (ParseException ex) {
                throw new RuntimeException("无法创建默认cron表达式", ex);
            }
        }

        ServerTickEvents.END_SERVER_TICK.register(s -> {
            if (s.getTicks() % 20 == 0) {
                checkSchedule(s);
            }
        });
    }

    private static void checkSchedule(MinecraftServer server) {
        Date now = new Date();
        if (now.after(nextTrigger)) {
            broadcastTime(server);
            nextTrigger = cron.getNextValidTimeAfter(now);
        }
    }

    private static void broadcastTime(MinecraftServer server) {
        String time = TimeProvider.getFormattedTime(server);
        String message = ConfigManager.config.messageFormat.replace("{time}", time);
        server.getPlayerManager().broadcast(Text.literal(message), false);
    }
}