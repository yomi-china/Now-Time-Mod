package com.yomi.nowtime;

import com.yomi.nowtime.config.ConfigManager;
import com.yomi.nowtime.scheduler.AnnouncementScheduler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NowTime implements ModInitializer {
	public static final String MOD_ID = "nowtime";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ConfigManager.loadConfig();
			AnnouncementScheduler.initialize(server);
		});
		LOGGER.info("Ding!Now Time Mod Initialized!");
	}
}