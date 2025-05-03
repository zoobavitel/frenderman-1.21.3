package net.zbavitel.frenderman.util;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.zbavitel.frenderman.Frenderman;

public class ModGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> TAMED_ENDERMAN_TRADES =
            GameRuleRegistry.register("tamedEndermanTrades", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> TAMED_ENDERMAN_ATTACK =
            GameRuleRegistry.register("tamedEndermanAttack", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.IntRule> TAMED_ENDERMAN_TRADE_LIMIT =
            GameRuleRegistry.register("tamedEndermanTradeLimit", GameRules.Category.MOBS, GameRuleFactory.createIntRule(16, 1));

    public static void registerModGameRules() {
        Frenderman.LOGGER.info("Registering GameRules for " + Frenderman.MOD_ID);
    }
}
