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
            val inputItem = inputOres.firstOrNull { !it.isEmpty } ?: return null
            val outputOneItem = outputOneOres.firstOrNull { !it.isEmpty } ?: return null
            val outputTwoItem = outputOresTwo.firstOrNull { !it.isEmpty } ?: ItemStack.EMPTY
            if (CrusherRecipes.getRecipeFromInput(inputItem) != null) return null
            val sizedOutputOne = outputOneItem.copy()
            sizedOutputOne.count = outputOneAmount
            val sizedOutputTwo = outputTwoItem.copy()
            sizedOutputTwo.count = outputTwoAmount
            return CrusherRecipe(inputItem, sizedOutputOne, sizedOutputTwo, outputTwoChance)
        }
    }
}


data class SmelterRecipe(val input: ItemStack, val outputStack: ItemStack) {
    companion object {
        fun createSmelterRecipe(
            input: NonNullList<ItemStack>,
            inputAmount: Int,
            output: NonNullList<ItemStack>,
            outputAmount: Int
        ): SmelterRecipe? {
            val inputItem = input.firstOrNull { !it.isEmpty } ?: return null
            val outputItem = output.firstOrNull { !it.isEmpty } ?: return null
            if (SmelterRecipes.getRecipeFromInput(inputItem) != null) return null
            val sizedInputItem = inputItem.copy()
            sizedInputItem.count = inputAmount
            val sizedOutputItem = outputItem.copy()
            sizedOutputItem.count = outputAmount
            return SmelterRecipe(sizedInputItem, sizedOutputItem)
        }
    }
}

object SmelterRecipes {
    private val smelterRecipes = ArrayList<SmelterRecipe>()

    private fun recipeCreator(inputOre: String, inputAmount: Int, outputOne: String, output1Amount: Int): SmelterRecipe? {
        val inputStack = OreDictionary.getOres(inputOre, false)
        val outputOneStack = OreDictionary.getOres(outputOne, false)
        return SmelterRecipe.createSmelterRecipe(inputStack, inputAmount, outputOneStack, output1Amount)
    }

    fun initSmelterRecipes() {
        arrayOf(
            recipeCreator("dustIron", 1, "ingotIron", 1),
            recipeCreator("dustGold", 1, "ingotGold", 1)
        )
            .filter { it != null }
            .forEach { smelterRecipes.add(it!!) }
        AdvancedFactory.logger?.log(Level.INFO, "Smelter Recipes $smelterRecipes")

    }

    fun getRecipeFromInput(input: ItemStack): SmelterRecipe? {
        return SmelterRecipes.smelterRecipes.firstOrNull { it.input.item == input.item }
    }
}

object CrusherRecipes {
    private val crusherRecipes = ArrayList<CrusherRecipe>()

    private fun recipeCreator(inputOre: String, outputOne: String, output1Amount: Int, outputTwo: String? = null, outputTwoAmount: Int = 0, outputTwoChance: Int = 0): CrusherRecipe? {
        val inputStack = OreDictionary.getOres(inputOre, false)
        val outputOneStack = OreDictionary.getOres(outputOne, false)
        val outputTwoStack = if (outputTwo != null) OreDictionary.getOres(outputTwo, false) else NonNullList.withSize(1, ItemStack.EMPTY)
        return CrusherRecipe.createCrusherRecipes(inputStack, outputOneStack, output1Amount, outputTwoStack, outputTwoAmount, outputTwoChance)
    }


    fun initCrusherRecipes() {
        arrayOf(
            recipeCreator("oreIron", "dustIron", 2, "dustGold", 1, 20),
            recipeCreator("oreGold", "dustGold", 2)
        )
            .filter { it != null }
            .forEach { crusherRecipes.add(it!!) }
        AdvancedFactory.logger?.log(Level.INFO, "Crusher Recipes $crusherRecipes")
    }

    fun getRecipeFromInput(input: ItemStack): CrusherRecipe? {
        return crusherRecipes.firstOrNull { it.input.item == input.item }
    }

    fun hasBlacklistedOutput(output: ItemStack): Boolean {
        return false
    }
}