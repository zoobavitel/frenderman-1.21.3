package net.zbavitel.frenderman.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zbavitel.frenderman.Frenderman1211;
import net.zbavitel.frenderman.entity.ModEntities;

public class ModItems {

    // Define your items
    public static final Item COPPER_COIN = registerItem("copper_coin", new Item(new Item.Settings()));

    // Define spawn egg for the TamedEndermanEntity
    public static final Item TAMED_ENDERMAN_SPAWN_EGG = registerItem("tamed_enderman_spawn_egg",
            new SpawnEggItem(
                    ModEntities.TAMED_ENDERMAN, // Custom entity type
                    0x1d1d21, // Background color (dark gray)
                    0x6e3f5e, // Spot color (purple)
                    new Item.Settings()
            )
    );

    // Helper method for registering items
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Frenderman1211.MOD_ID, name), item);
    }

    // Method to register all mod items
    public static void registerModItems() {
        Frenderman1211.LOGGER.info("Registering Mod Items for " + Frenderman1211.MOD_ID);

        // Add items to the Ingredients tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(COPPER_COIN);
        });

        // Add spawn egg to the spawn eggs tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.add(TAMED_ENDERMAN_SPAWN_EGG);
        });
    }
}
