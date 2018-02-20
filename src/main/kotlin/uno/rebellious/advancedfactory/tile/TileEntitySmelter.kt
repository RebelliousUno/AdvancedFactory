package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos

class TileEntitySmelter : TileEntity(), IAdvancedFactoryTile, ITickable {

    private var smelterInput = NonNullList.withSize(1, ItemStack.EMPTY)
    private var smelterOutput = NonNullList.withSize(1, ItemStack.EMPTY)
    private var currentSmeltingItem = ItemStack.EMPTY
    private var currentCookingTime = 0

    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override val factoryBlockType: String = "Smelter"

    override fun update() {
        //Do smelting operation
        if (currentSmeltingItem != ItemStack.EMPTY) {
            if (currentCookingTime == 0) {
                smeltItem()
            } else if (currentCookingTime > 0) {
                currentCookingTime--
            }
        } else if (canSmelt()) {
            moveItemToSmelt()
            currentCookingTime = getCookTime()
        }
    }

    private fun moveItemToSmelt() {
        currentSmeltingItem = smelterInput[0].copy()
        currentSmeltingItem.count = 1
        smelterInput[0].shrink(1)
    }

    private fun canSmelt(): Boolean {
        return !(smelterInput[0].isEmpty || FurnaceRecipes.instance().getSmeltingResult(smelterInput[0]).isEmpty)
    }

    private fun getCookTime(): Int = 200

    private fun smeltItem() {
        if (currentSmeltingItem.isEmpty) return
        val result = FurnaceRecipes.instance().getSmeltingResult(currentSmeltingItem)
        when {
            smelterOutput[0].isEmpty -> {
                smelterOutput[0] = result.copy()
                currentSmeltingItem = ItemStack.EMPTY
            }
            smelterOutput[0].item == result.item -> {
                smelterOutput[0].grow(result.count)
                currentSmeltingItem = ItemStack.EMPTY
            }
        }
    }


}