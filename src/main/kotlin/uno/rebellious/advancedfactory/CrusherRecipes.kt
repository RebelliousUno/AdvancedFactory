package uno.rebellious.advancedfactory.recipe


import net.minecraft.item.ItemStack

data class CrusherRecipe(
    val input: ItemStack,
    val outputOne: ItemStack,
    val outputTwo: ItemStack,
    val outputTwoChance: ItemStack
)

data class SmelterRecipe(val input: ItemStack, val outputStack: ItemStack)

class CrusherRecipes {

    val crusherRecipes = ArrayList<CrusherRecipe>()

    fun registerCrusherRecipes() {

    }
}

class SmelterRecipes {
    fun registerSmelterRecipes() {

    }
}