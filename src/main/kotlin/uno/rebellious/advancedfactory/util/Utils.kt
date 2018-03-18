package uno.rebellious.advancedfactory.util

import net.minecraft.item.ItemStack
import net.minecraft.world.World

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

/*Extensions */

val World.isClient: Boolean
    get() {
        return this.isRemote
    }

val World.isServer: Boolean
    get() {
        return !this.isRemote
    }
