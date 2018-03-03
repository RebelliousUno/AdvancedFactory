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

    fun initSmelterRecipes() {
        arrayOf(
            SmelterRecipe.createSmelterRecipe(
                OreDictionary.getOres("dustIron"),
                1,
                OreDictionary.getOres("ingotIron"),
                1
            ),
            SmelterRecipe.createSmelterRecipe(
                OreDictionary.getOres("dustGold"),
                1,
                OreDictionary.getOres("ingotGold"),
                1
            )
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

    fun initCrusherRecipes() {
        arrayOf(
            CrusherRecipe.createCrusherRecipes(
                OreDictionary.getOres("oreIron", false),
                OreDictionary.getOres("dustIron", false),
                2,
                OreDictionary.getOres("dustGold", false),
                1,
                20
            ),
            CrusherRecipe.createCrusherRecipes(
                OreDictionary.getOres("oreGold", false),
                OreDictionary.getOres("dustGold", false),
                2,
                NonNullList.withSize(1, ItemStack.EMPTY), 0, 0
            )
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