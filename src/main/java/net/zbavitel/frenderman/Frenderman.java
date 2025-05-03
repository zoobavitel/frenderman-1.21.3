package net.zbavitel.frenderman;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.fabricmc.api.ModInitializer;
import net.zbavitel.frenderman.common.item.ModItemGroups;
import net.zbavitel.frenderman.common.item.ModItems;
import net.zbavitel.frenderman.entity.ModEntities;
import net.zbavitel.frenderman.config.FrendermanConfig;
import net.zbavitel.frenderman.util.ModGameRules;
import net.zbavitel.frenderman.util.MiscRegisters;
import net.zbavitel.frenderman.util.ServerTickEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Frenderman implements ModInitializer {
	public static final String MOD_ID = "frenderman";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Frenderman Mod");
		ServerTickEvents.END_SERVER_TICK.register(new ServerTickEventHandler());

		// Load configuration
		FrendermanConfig.load();

		// Register item groups
		ModItemGroups.registerItemGroups();

		// Register items and entities
		ModItems.registerModItems();
		ModEntities.registerModEntities();

		// Register game rules
		ModGameRules.registerModGameRules();

		// Initialize entity replacements
		MiscRegisters.initializeReplacements();

		LOGGER.info("Frenderman Mod has been initialized!");
	}
}