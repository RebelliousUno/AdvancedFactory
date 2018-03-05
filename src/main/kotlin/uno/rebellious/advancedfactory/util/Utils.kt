package uno.rebellious.advancedfactory.util

import net.minecraft.item.ItemStack

enum class Types(val unlocalizedName: kotlin.String) {
    INPUT_HATCH("Input Hatch"),
    OUTPUT_HATCH("Output Hatch"),
    SMELTER("Smelter"),
    CRUSHER("Crusher"),
    CONTROLLER("Crusher")
}

object Helpers {
    fun spaceInStack(stack: ItemStack): Int {
        return stack.maxStackSize - stack.count
    }
}
