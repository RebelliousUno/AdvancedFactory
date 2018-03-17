package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.block.BlockController

abstract class TileEntityHatch : TileEntityAdvancedFactory(), ITickable {
    private var _controller: BlockPos? = null
    override var itemInventory = NonNullList.withSize(2, ItemStack.EMPTY)


    override var controllerTile: BlockPos?
        get() = this._controller
        set(value) {
            this._controller = value
        }

    override fun update() {
        //Check if still has a valid controller
        //If not then remove it
        if (controllerTile != null) {
            val block = this.world.getBlockState(controllerTile!!).block
            if (block !is BlockController) controllerTile = null
        }
    }

    protected fun moveItemsFromInputToOutput() {
        if (!inputStack.isEmpty) {
            if (outputStack.isEmpty) { // Something to move and space to move it
                outputStack = inputStack.copy()
                inputStack = ItemStack.EMPTY
            } else if (inputStack.item == outputStack.item) {
                val outputSpace = outputStack.maxStackSize - outputStack.count
                val inputSize = inputStack.count
                if (outputSpace <= inputSize) {
                    inputStack.shrink(outputSpace)
                    outputStack.grow(outputSpace)
                } else {
                    outputStack.grow(inputSize)
                    inputStack = ItemStack.EMPTY
                }
            }
        }
    }
}