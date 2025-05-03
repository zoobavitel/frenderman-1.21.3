package net.zbavitel.frenderman.common.trades;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.zbavitel.frenderman.common.item.ModItems;

import java.util.Optional;

public class TradeFactoryRegistry {

    public static final TradeOffers.Factory[] MINER_TRADES = new TradeOffers.Factory[]{
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 3),
                    Optional.empty(),
                    new ItemStack(Items.IRON_ORE, 6),
                    10, 5, 0.05f
            ),
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 5),
                    Optional.empty(),
                    new ItemStack(Items.GOLD_ORE, 3),
                    10, 5, 0.05f
            ),
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 12),
                    Optional.empty(),
                    new ItemStack(Items.DIAMOND, 1),
                    5, 10, 0.2f
            )
    };

    // This used to pull from vanilla trades, but VILLAGER_TRADES was removed. Replace with custom entries.
    public static final TradeOffers.Factory[] MERCHANT_TRADES = new TradeOffers.Factory[]{
            (entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 5),
                    Optional.empty(),
                    new ItemStack(Items.IRON_SWORD),
                    10, 5, 0.05f
            ),
            (entity, random) -> new TradeOffer(
                    new TradedItem(Items.EMERALD, 8),
                    Optional.empty(),
                    new ItemStack(Items.SHIELD),
                    10, 5, 0.05f
            )
    };

    public static final TradeOffers.Factory[] ENCHANTER_TRADES = new TradeOffers.Factory[]{
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 2),
                    Optional.empty(),
                    new ItemStack(Items.PAPER, 8),
                    10, 2, 0.05f
            ),
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 4),
                    Optional.empty(),
                    new ItemStack(Items.BOOK, 3),
                    10, 4, 0.05f
            ),
            (entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.COPPER_COIN, 10),
                    Optional.empty(),
                    new ItemStack(Items.EXPERIENCE_BOTTLE, 2),
                    5, 10, 0.2f
            )
    };

    // `createMapForStructure()` was removed, so we'll leave these blank for now.
    public static final TradeOffers.Factory[] EXPLORER_TRADES = new TradeOffers.Factory[]{
            // TODO: Implement structure-map trades manually using Cartography logic
    };

    public static TradeOffers.Factory[] getFactoriesForRole(String roleName) {
        return switch (roleName) {
            case "MINER" -> MINER_TRADES;
            case "MERCHANT" -> MERCHANT_TRADES;
            case "ENCHANTER" -> ENCHANTER_TRADES;
            case "EXPLORER" -> EXPLORER_TRADES;
            default -> new TradeOffers.Factory[0];
        };
    }
}
