package uno.rebellious.advancedfactory.util

import net.minecraft.item.ItemStack

enum class Types {
    INPUT_HATCH, OUTPUT_HATCH, SMELTER, CRUSHER, CONTROLLER
}

object Helpers {
    fun spaceInStack(stack: ItemStack): Int {
        return stack.maxStackSize - stack.count
    }
}
