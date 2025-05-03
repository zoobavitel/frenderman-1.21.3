package net.zbavitel.frenderman.common.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zbavitel.frenderman.Frenderman;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> COPPER_COIN_ITEMS_GROUP = RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            Identifier.of(Frenderman.MOD_ID + ":copper_coin_items")
    );

    public static void registerItemGroups() {
        Registry.register(Registries.ITEM_GROUP, COPPER_COIN_ITEMS_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.COPPER_COIN))
                .displayName(Text.translatable("itemgroup." + Frenderman.MOD_ID + ".copper_coin_items"))
                .build());

        ItemGroupEvents.modifyEntriesEvent(COPPER_COIN_ITEMS_GROUP).register(entries -> {
            entries.add(ModItems.COPPER_COIN);
            entries.add(ModItems.FRENDERMAN_SPAWN_EGG); // Add spawn egg to the custom group
        });

        Frenderman.LOGGER.info("Registering Item Group for " + Frenderman.MOD_ID);
    }

}