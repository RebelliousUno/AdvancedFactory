package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos

class TileEntitySmelter : TileEntity(), IAdvancedFactoryTile, ITickable {
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(3, ItemStack.EMPTY)

    override var inputStack: ItemStack
        get() = itemInventory[0]
        set(value) {
            itemInventory[0] = value
        }

    override var outputStack: ItemStack
        get() = itemInventory[1]
        set(value) {
            itemInventory[1] = value
        }

    private var currentSmeltingItem
        get() = itemInventory[2]
        set(value) {
            itemInventory[2] = value
        }

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
        currentSmeltingItem = inputStack.copy()
        currentSmeltingItem.count = 1
        inputStack.shrink(1)
    }

    private fun canSmelt(): Boolean {
        return !(inputStack.isEmpty || FurnaceRecipes.instance().getSmeltingResult(inputStack).isEmpty)
    }

    private fun getCookTime(): Int = 200

    private fun smeltItem() {
        if (currentSmeltingItem.isEmpty) return
        val result = FurnaceRecipes.instance().getSmeltingResult(currentSmeltingItem)
        when {
            outputStack.isEmpty -> {
                outputStack = result.copy()
                currentSmeltingItem = ItemStack.EMPTY
            }
            outputStack.item == result.item -> {
                outputStack.grow(result.count)
                currentSmeltingItem = ItemStack.EMPTY
            }
        }
    }


}