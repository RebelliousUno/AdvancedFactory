package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
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