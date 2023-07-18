package dev.igalaxy.emipowerjuices

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier

class PowerJuicesJuicerRecipe(private val input: List<EmiIngredient>, private val output: EmiStack, private val id: Identifier) : EmiRecipe {
    companion object {
        val BACKGROUND = Identifier("minecraft", "textures/gui/container/hopper.png")
        val WIDGETS = Identifier("emi", "textures/gui/widgets.png")
    }

    override fun getCategory(): EmiRecipeCategory {
        return EmiPowerJuicesPlugin.JUICER_CATEGORY
    }

    override fun getId(): Identifier? {
        return id
    }

    override fun getInputs(): MutableList<EmiIngredient> {
        val newInputs = input.toMutableList()
        newInputs.add(EmiIngredient.of(Ingredient.ofItems(Items.GLASS_BOTTLE)))
        return newInputs
    }

    override fun getOutputs(): MutableList<EmiStack> {
        return mutableListOf(output)
    }

    override fun getDisplayWidth(): Int {
        return 146
    }

    override fun getDisplayHeight(): Int {
        return 26
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(BACKGROUND, 0, 3, 91, 20, 42, 18)
        widgets.addTexture(WIDGETS, 93, 5, 25, 17, 44, 0)
        widgets.addTexture(WIDGETS, 119, 0, 26, 26, 18, 0)
        widgets.addSlot(input.getOrElse(0) { EmiIngredient.of(Ingredient.EMPTY) }, 1, 4).drawBack(false)
        widgets.addSlot(input.getOrElse(1) { EmiIngredient.of(Ingredient.EMPTY) }, 19, 4).drawBack(false)
        widgets.addSlot(input.getOrElse(2) { EmiIngredient.of(Ingredient.EMPTY) }, 37, 4).drawBack(false)
        widgets.addSlot(input.getOrElse(3) { EmiIngredient.of(Ingredient.EMPTY) }, 55, 4).drawBack(false)
        widgets.addSlot(EmiStack.of(Items.GLASS_BOTTLE), 73, 4).drawBack(false)
        widgets.addSlot(output, 123, 4).drawBack(false)
    }
}
