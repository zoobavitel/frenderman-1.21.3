package net.zbavitel.frenderman.util;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.zbavitel.frenderman.Frenderman;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> FRENDERMAN_TRADES =
            GameRuleRegistry.register("FrendermanTrades", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> FRENDERMAN_ATTACK =
            GameRuleRegistry.register("FrendermanAttack", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.IntRule> FRENDERMAN_TRADE_LIMIT =
            GameRuleRegistry.register("FrendermanTradeLimit", GameRules.Category.MOBS, GameRuleFactory.createIntRule(16, 1));

    public static void registerModGameRules() {
        Frenderman.LOGGER.info("Registering GameRules for " + Frenderman.MOD_ID);
    }
}
