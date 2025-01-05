package net.zbavitel.frenderman.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.zbavitel.frenderman.Frenderman1211;

public class ModItemGroups {

    public static final ItemGroup COPPER_COIN_ITEMS_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Frenderman1211.MOD_ID, "copper_coin_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.COPPER_COIN))
                    .displayName(Text.translatable("itemgroup.frenderman1211.copper_coin_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.COPPER_COIN);
                    }).build());


    public static void registerItemGroups() {
        Frenderman1211.LOGGER.info("Registering Item Group for " + Frenderman1211.MOD_ID);
    }
}
