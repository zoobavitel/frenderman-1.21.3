package net.zbavitel.frenderman;

import net.fabricmc.api.ModInitializer;

import net.zbavitel.frenderman.item.ModItemGroups;
import net.zbavitel.frenderman.item.ModItems;
import net.zbavitel.frenderman.entity.ModEntities
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Frenderman1211 implements ModInitializer {
	public static final String MOD_ID = "frenderman";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModEntities.registerEntities(); // Register custom entities
	}
}