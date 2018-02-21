package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

interface IAdvancedFactoryTile {
    var controllerTile: TileEntityController?
    val factoryBlockType: String
    var itemInventory: NonNullList<ItemStack>

    var inputStack: ItemStack
    var outputStack: ItemStack

}
