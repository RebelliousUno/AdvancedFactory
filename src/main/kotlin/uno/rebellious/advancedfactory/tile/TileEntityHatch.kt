package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.block.BlockController

abstract class TileEntityHatch : TileEntity(), ITickable, IAdvancedFactoryTile {
    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var itemInventory = NonNullList.withSize(2, ItemStack.EMPTY)

    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override fun update() {
        //Check if still has a valid controller
        //If not then remove it
        if (controllerTile != null && controllerTilePos != null) {
            val block = this.world.getBlockState(controllerTilePos!!).block
            if (block !is BlockController) controllerTile = null
        }
    }

    protected fun moveItemsFromInputToOutput() {
        if (!itemInventory[0].isEmpty) {
            if (itemInventory[1].isEmpty) { // Something to move and space to move it
                itemInventory[1] = itemInventory[0].copy()
                itemInventory[0] = ItemStack.EMPTY
            } else if (itemInventory[0].item == itemInventory[1].item) {
                val outputSpace = itemInventory[1].maxStackSize - itemInventory[1].count
                val inputSize = itemInventory[0].count
                if (outputSpace <= inputSize) {
                    itemInventory[0].shrink(outputSpace)
                    itemInventory[1].grow(outputSpace)
                } else {
                    itemInventory[1].grow(inputSize)
                    itemInventory[0] = ItemStack.EMPTY
                }
            }
        }
    }
}