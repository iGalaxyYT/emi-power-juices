package dev.igalaxy.emipowerjuices

import com.google.common.collect.Lists
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.handler.EmiCraftContext
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler
import net.minecraft.screen.HopperScreenHandler
import net.minecraft.screen.slot.Slot

class PowerJuicesJuicerRecipeHandler : StandardRecipeHandler<HopperScreenHandler> {
    override fun supportsRecipe(recipe: EmiRecipe): Boolean {
        return recipe.category == EmiPowerJuicesPlugin.JUICER_CATEGORY && recipe.supportsRecipeTree()
    }

    override fun canCraft(recipe: EmiRecipe, context: EmiCraftContext<HopperScreenHandler>): Boolean {
        return context.screen.title.string.contains("Juicer") && super.canCraft(recipe, context)
    }

    override fun getCraftingSlots(handler: HopperScreenHandler): MutableList<Slot> {
        val list: ArrayList<Slot> = Lists.newArrayList<Slot>()
        for (i in 0..5) {
            list.add(handler.getSlot(i))
        }
        return list
    }

    override fun getOutputSlot(handler: HopperScreenHandler): Slot? {
        return handler.getSlot(5)
    }

    override fun getInputSources(handler: HopperScreenHandler): MutableList<Slot> {
        val list: ArrayList<Slot> = Lists.newArrayList<Slot>()
        for (i in 0..5) {
            list.add(handler.getSlot(i))
        }
        val invStart = 6
        for (i in invStart..(invStart + 34)) {
            list.add(handler.getSlot(i))
        }
        return list
    }
}
