package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.util.Types

interface IAdvancedFactoryTile {
    var itemInventory: NonNullList<ItemStack> //Needs to always be at least 2...

    var inputStack: ItemStack
        get() = itemInventory[0]
        set(value) {
            itemInventory[0] = value
        }

    var outputStack: ItemStack
        get() = itemInventory[1]
        set(value) {
            itemInventory[1] = value
        }
}
