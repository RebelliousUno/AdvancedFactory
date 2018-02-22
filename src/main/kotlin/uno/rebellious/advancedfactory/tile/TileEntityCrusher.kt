package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraftforge.oredict.OreDictionary
import uno.rebellious.advancedfactory.recipe.CrusherRecipes

class TileEntityCrusher : TileEntity(), IAdvancedFactoryTile, ITickable {
    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null

    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override val factoryBlockType: String = "inputHatch"

    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(3, ItemStack.EMPTY)
    var currentCrushingItem
        get() = itemInventory[3]
        set(value) {
            itemInventory[3] = value
        }


    private var currentCrushingTime = 0

    override fun update() {
        //Do crushing operation
        if (currentCrushingItem != ItemStack.EMPTY) {
            if (currentCrushingTime == 0) {
                crushItem()
            } else if (currentCrushingTime > 0) {
                currentCrushingTime--
            }
        } else if (canCrush()) {
            moveItemToCrush()
            currentCrushingTime = getCrushTime()
        }
    }

    private fun canCrush() : Boolean {
        return CrusherRecipes
    }

    private fun getCrushTime() = 200

    private fun crushItem() {

    }

}
