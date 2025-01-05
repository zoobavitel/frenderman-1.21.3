package net.zbavitel.frenderman;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.advancement.criterion.TameAnimalCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.zbavitel.frenderman.entity.ModEntities;
import net.zbavitel.frenderman.item.ModItems;

import java.util.function.Consumer;

public class Frenderman1211DataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		// Add the advancement provider to the data generator
		fabricDataGenerator.createPack().addProvider(AdvancementGenerator::new);
	}

	private static class AdvancementGenerator extends FabricAdvancementProvider {
		public AdvancementGenerator(FabricDataGenerator dataGenerator) {
			super(dataGenerator);
		}

		@Override
		protected void generateAdvancement(RegistryWrapper.WrapperLookup lookup, Consumer<AdvancementEntry> consumer) {
			// Tame Enderman Advancement
			AdvancementEntry tameEnderman = Advancement.Builder.create()
					.display(
							ModItems.COPPER_COIN, // Icon
							Text.literal("Friend of the End"), // Title
							Text.literal("Tame an Enderman using a Copper Coin."), // Description
							null, // Background (optional)
							AdvancementFrame.TASK,
							true, // Show toast
							true, // Announce to chat
							false // Hidden
					)
					.criterion(
							"tame_enderman",
							TameAnimalCriterion.Conditions.create(
									EntityPredicate.Builder.create().type(ModEntities.TAMED_ENDERMAN).build()
							)
					)
					.build("frenderman:tame_enderman");

			// Feed Enderman Flower Advancement
			AdvancementEntry feedFlower = Advancement.Builder.create()
					.display(
							Items.POPPY, // Icon
							Text.literal("Flower of Friendship"), // Title
							Text.literal("Feed a flower to a Tamed Enderman."), // Description
							null, // Background (optional)
							AdvancementFrame.TASK,
							true, // Show toast
							true, // Announce to chat
							false // Hidden
					)
					.criterion(
							"feed_flower",
							PlayerInteractedWithEntityCriterion.Conditions.create(
									EntityPredicate.Builder.create().type(ModEntities.TAMED_ENDERMAN).build(),
									ItemPredicate.Builder.create().tag(ItemTags.FLOWERS).build()
							)
					)
					.build("frenderman:feed_flower");

			// Add advancements to the consumer
			consumer.accept(tameEnderman);
			consumer.accept(feedFlower);
		}
	}
}
