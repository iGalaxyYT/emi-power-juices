package dev.igalaxy.emipowerjuices

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import org.quiltmc.qkl.library.nbt.set
import org.quiltmc.qkl.library.text.buildText
import org.quiltmc.qkl.library.text.italic
import org.quiltmc.qkl.library.text.literal

class EmiPowerJuicesPlugin : EmiPlugin {
    companion object {
        private val JUICER_ICON: EmiStack = EmiStack.of(Items.HOPPER)
        val JUICER_CATEGORY = EmiRecipeCategory(Identifier("mcpeachpies_power_juices", "juicer"), JUICER_ICON)
    }

    override fun register(registry: EmiRegistry) {
        registry.addCategory(JUICER_CATEGORY)
        registry.addWorkstation(JUICER_CATEGORY, JUICER_ICON)

        fun potionEffect(id: Int, duration: Int = -1, amplifier: Int = -1): NbtCompound {
            val nbt = NbtCompound()
            nbt.putInt("Id", id)
            if (duration != -1) nbt.putInt("Duration", duration)
            if (amplifier != -1) nbt.putInt("Amplifier", amplifier)
            return nbt
        }

        fun juice(id: String, name: String, color: Int, effs: List<NbtCompound>): ItemStack {
            val stack = Items.POTION.defaultStack
            val nbt = NbtCompound()
            nbt["Potion"] = "mcpeachpies_power_juices:${id}"
            nbt["CustomPotionColor"] = color
            val effects = NbtList()
            effects.addAll(effs)
            nbt["CustomPotionEffects"] = effects
            stack.nbt = nbt
            stack.setCustomName(buildText {
                italic(false) {
                    literal(name)
                }
            })
            return stack
        }

        fun splashJuice(id: String, name: String, color: Int, effs: List<NbtCompound>): ItemStack {
            val stack = Items.SPLASH_POTION.defaultStack
            val nbt = NbtCompound()
            nbt["Potion"] = "mcpeachpies_power_juices:${id}"
            nbt["CustomPotionColor"] = color
            val effects = NbtList()
            effects.addAll(effs)
            nbt["CustomPotionEffects"] = effects
            stack.nbt = nbt
            stack.setCustomName(buildText {
                italic(false) {
                    literal("Splash $name")
                }
            })
            return stack
        }

        fun lingeringJuice(id: String, name: String, color: Int, effs: List<NbtCompound>): ItemStack {
            val stack = Items.LINGERING_POTION.defaultStack
            val nbt = NbtCompound()
            nbt["Potion"] = "mcpeachpies_power_juices:${id}"
            nbt["CustomPotionColor"] = color
            val effects = NbtList()
            effects.addAll(effs)
            nbt["CustomPotionEffects"] = effects
            stack.nbt = nbt
            stack.setCustomName(buildText {
                italic(false) {
                    literal("Lingering $name")
                }
            })
            return stack
        }

        fun juiceTippedArrow(id: String, name: String, color: Int, effs: List<NbtCompound>): ItemStack {
            val stack = Items.TIPPED_ARROW.defaultStack
            val nbt = NbtCompound()
            nbt["Potion"] = "mcpeachpies_power_juices:${id}"
            nbt["CustomPotionColor"] = color
            val effects = NbtList()
            effects.addAll(effs)
            nbt["CustomPotionEffects"] = effects
            stack.nbt = nbt
            stack.setCustomName(buildText {
                italic(false) {
                    literal("Arrow of $name")
                }
            })
            return stack
        }

        val juices = mutableListOf<EmiStack>()
        val splashJuices = mutableListOf<EmiStack>()
        val lingeringJuices = mutableListOf<EmiStack>()
        val tippedArrows = mutableListOf<EmiStack>()

        fun registerJuice(id: String, name: String, color: Int, effs: List<NbtCompound>, ingredients: List<EmiIngredient>) {
            val juiceItem = EmiStack.of(juice(id, name, color, effs))
            val splashJuiceItem = EmiStack.of(splashJuice(id, name, color, effs))
            val lingeringJuiceItem = EmiStack.of(lingeringJuice(id, name, color, effs))
            val tippedArrowItem = EmiStack.of(juiceTippedArrow(id, name, color, effs))

            juices.add(juiceItem)
            splashJuices.add(splashJuiceItem)
            lingeringJuices.add(lingeringJuiceItem)
            tippedArrows.add(tippedArrowItem)
            registry.addRecipe(
                PowerJuicesJuicerRecipe(ingredients, juiceItem, Identifier("mcpeachpies_power_juices", id))
            )
            registry.addRecipe(
                EmiBrewingRecipe(juiceItem, EmiIngredient.of(Ingredient.ofItems(Items.GUNPOWDER)), splashJuiceItem, Identifier("mcpeachpies_power_juices", "splash_$id"))
            )
            registry.addRecipe(
                EmiBrewingRecipe(splashJuiceItem, EmiIngredient.of(Ingredient.ofItems(Items.DRAGON_BREATH)), lingeringJuiceItem, Identifier("mcpeachpies_power_juices", "lingering_$id"))
            )
            val arrow = EmiIngredient.of(Ingredient.ofItems(Items.ARROW))
            val lingeringIngredient = EmiIngredient.of(Ingredient.ofStacks(lingeringJuice(id, name, color, effs)))
            val tippedArrowOutput = EmiStack.of(juiceTippedArrow(id, name, color, effs))
            tippedArrowOutput.amount = 8
            registry.addRecipe(
                EmiCraftingRecipe(listOf(
                    arrow, arrow, arrow,
                    arrow, lingeringIngredient, arrow,
                    arrow, arrow, arrow
                ), tippedArrowOutput, Identifier("mcpeachpies_power_juices", "tipped_arrow_$id")
                )
            )
        }

        registerJuice("apple_juice", "Apple Juice", 16626446, listOf(
            potionEffect(1, 1200),
            potionEffect(8, 1200, 1),
            potionEffect(23, amplifier = 3)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.APPLE), 3)
        ))
        registerJuice("shiny_apple_juice", "Shiny Apple Juice", 16761924, listOf(
            potionEffect(1, 2400),
            potionEffect(8, 2400, 1),
            potionEffect(22, 2400),
            potionEffect(10, 100, 1),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.GOLDEN_APPLE), 1)
        ))
        registerJuice("sparkling_apple_juice", "Sparkling Apple Juice", 16765009, listOf(
            potionEffect(1, 2400, 1),
            potionEffect(8, 2400, 1),
            potionEffect(22, 2400, 3),
            potionEffect(10, 6000, 4),
            potionEffect(12, 6000),
            potionEffect(11, 6000),
            potionEffect(23, amplifier = 19)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.ENCHANTED_GOLDEN_APPLE), 1)
        ))
        registerJuice("carrot_juice", "Carrot Juice", 15895325, listOf(
            potionEffect(12, 2400),
            potionEffect(16, 2400),
            potionEffect(23, amplifier = 5),
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.CARROT), 3)
        ))
        registerJuice("shiny_carrot_juice", "Shiny Carrot Juice", 15895325, listOf(
            potionEffect(12, 9600),
            potionEffect(16, 9600),
            potionEffect(23, amplifier = 5),
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.GOLDEN_CARROT), 1)
        ))
        registerJuice("melon_juice", "Melon Juice", 16668757, listOf(
            potionEffect(6),
            potionEffect(23, amplifier = 9),
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.MELON), 5)
        ))
        registerJuice("shiny_melon_juice", "Shiny Melon Juice", 16668757, listOf(
            potionEffect(6, amplifier = 1),
            potionEffect(23, amplifier = 9),
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.GLISTERING_MELON_SLICE), 2)
        ))
        registerJuice("pumpkin_juice", "Pumpkin Juice", 15372035, listOf(
            potionEffect(3, 2400),
            potionEffect(23, amplifier = 5),
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.PUMPKIN), 2),
            EmiIngredient.of(Ingredient.ofItems(Items.SUGAR), 1)
        ))
        registerJuice("glowing_pumpkin_juice", "Glowing Pumpkin Juice", 15372035, listOf(
            potionEffect(3, 3000, 1),
            potionEffect(24, 3000),
            potionEffect(23, amplifier = 5)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.PUMPKIN), 2),
            EmiIngredient.of(Ingredient.ofItems(Items.GLOWSTONE_DUST), 1),
            EmiIngredient.of(Ingredient.ofItems(Items.SUGAR), 1)
        ))
        registerJuice("cactus_juice", "Cactus Juice", 11523706, listOf(
            potionEffect(11, 1800),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.CACTUS), 3),
        ))
        registerJuice("concentrated_cactus_juice", "Concentrated Cactus Juice", 10799473, listOf(
            potionEffect(11, 1800, 1),
            potionEffect(9, 180, 0),
            potionEffect(23, amplifier = 3)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.CACTUS), 3),
            EmiIngredient.of(Ingredient.ofStacks(juice("cactus_juice", "Cactus Juice", 11523706, listOf(
                potionEffect(11, 1800),
                potionEffect(23, amplifier = 1)
            )))),
        ))
        registerJuice("beetroot_juice", "Beetroot Juice", 13179962, listOf(
            potionEffect(21, 1800),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.BEETROOT), 4),
        ))
        registerJuice("beetroot_cream", "Beetroot Cream", 12082863, listOf(
            potionEffect(21, 3600),
            potionEffect(23, amplifier = 5)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.BEETROOT), 8),
            EmiIngredient.of(Ingredient.ofItems(Items.MILK_BUCKET), 1),
        ))
        registerJuice("chorus_juice", "Chorus Juice", 16777215, listOf(
            potionEffect(25, 250),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.CHORUS_FRUIT), 3),
        ))
        registerJuice("popping_chorus_juice", "Popping Chorus Juice", 16245503, listOf(
            potionEffect(25, 24, 48),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.POPPED_CHORUS_FRUIT), 3),
        ))
        registerJuice("magic_mushroom_juice", "Magic Mushroom Juice", 9581977, listOf(
            potionEffect(9, 600),
            potionEffect(8, 1200, 5),
            potionEffect(4, 1200),
            potionEffect(24, 1200),
            potionEffect(19, 1200),
            potionEffect(10, 1200, 1),
            potionEffect(23, amplifier = 3)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.RED_MUSHROOM), 3),
            EmiIngredient.of(Ingredient.ofItems(Items.BROWN_MUSHROOM), 3),
            EmiIngredient.of(Ingredient.ofItems(Items.FERMENTED_SPIDER_EYE), 3),
        ))
        registerJuice("hot_chocolate", "Hot Chocolate", 6376483, listOf(
            potionEffect(1, 360, 2),
            potionEffect(23, amplifier = 3)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.MILK_BUCKET), 1),
            EmiIngredient.of(Ingredient.ofItems(Items.COCOA_BEANS), 1),
            EmiIngredient.of(Ingredient.ofItems(Items.SUGAR), 1),
        ))
        registerJuice("ocean_juice", "Ocean Juice", 6149286, listOf(
            potionEffect(29, 2400),
            potionEffect(30, 2400),
            potionEffect(23, amplifier = 1)
        ), listOf(
            EmiIngredient.of(Ingredient.ofItems(Items.KELP), 8),
            EmiIngredient.of(Ingredient.ofItems(Items.SEAGRASS), 4),
        ))

        juices.forEach { emiStack ->
            registry.addEmiStack(emiStack.comparison(Comparison.compareNbt()))
        }
        splashJuices.forEach { emiStack ->
            registry.addEmiStack(emiStack.comparison(Comparison.compareNbt()))
        }
        lingeringJuices.forEach { emiStack ->
            registry.addEmiStack(emiStack.comparison(Comparison.compareNbt()))
        }
        tippedArrows.forEach { emiStack ->
            registry.addEmiStack(emiStack.comparison(Comparison.compareNbt()))
        }
    }
}
