package uno.rebellious.advancedfactory.recipe

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory

data class CrusherRecipe(
    val input: ItemStack,
    val outputOne: ItemStack,
    val outputTwo: ItemStack,
    val outputTwoChance: Int
) {
    companion object {
        fun createCrusherRecipes(
            inputOres: NonNullList<ItemStack>,
            outputOneOres: NonNullList<ItemStack>,
            outputOneAmount: Int,
            outputOresTwo: NonNullList<ItemStack>,
            outputTwoAmount: Int,
            outputTwoChance: Int
        ): CrusherRecipe? {
            AdvancedFactory.logger?.log(Level.INFO, inputOres)
            AdvancedFactory.logger?.log(Level.INFO, outputOneOres)
            AdvancedFactory.logger?.log(Level.INFO, outputOresTwo)
            var inputItem = inputOres.firstOrNull { !it.isEmpty }
            var outputOneItem = outputOneOres.firstOrNull{ !it.isEmpty }
            var outputTwoItem = outputOresTwo.firstOrNull { !it.isEmpty }
            if (inputItem == null || outputOneItem == null) return null
            if (CrusherRecipes.getRecipeFromInput(inputItem) != null) return null
            if (outputTwoItem == null) outputTwoItem = ItemStack.EMPTY
            var sizedOutputOne = outputOneItem.copy()
            sizedOutputOne.count = outputOneAmount
            var sizedOutputTwo = outputTwoItem!!.copy() //Shouldn't be null should be ItemStack.EMPTY or something
            sizedOutputTwo.count = outputTwoAmount
            return CrusherRecipe(inputItem, sizedOutputOne, sizedOutputTwo, outputTwoChance)
        }
    }
}


data class SmelterRecipe(val input: ItemStack, val outputStack: ItemStack)

object CrusherRecipes {
    val crusherRecipes = ArrayList<CrusherRecipe>()

    fun registerCrusherRecipes() {
        arrayOf(
            CrusherRecipe.createCrusherRecipes(OreDictionary.getOres("oreIron", false), OreDictionary.getOres("dustIron", false), 2, OreDictionary.getOres("dustGold", false), 1, 20)
        )
            .filter {it != null }
            .forEach{ crusherRecipes.add(it!!) }
        AdvancedFactory.logger?.log(Level.INFO, "Crusher Recipes $crusherRecipes")
    }

    fun getRecipeFromInput(input: ItemStack): CrusherRecipe? {
        return crusherRecipes.firstOrNull { it.input.item == input.item }
    }

    fun hasBlacklistedOutput(output: ItemStack): Boolean {
        return false
    }
}

object SmelterRecipes {
    fun registerSmelterRecipes() {

    }
}