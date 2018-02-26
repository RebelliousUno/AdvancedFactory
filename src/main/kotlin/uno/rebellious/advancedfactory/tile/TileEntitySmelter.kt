package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.recipe.SmelterRecipes
import uno.rebellious.advancedfactory.util.Types

class TileEntitySmelter : TileEntity(), IAdvancedFactoryTile, ITickable {
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(3, ItemStack.EMPTY)

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

    override val factoryBlockType = Types.SMELTER

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
        val emptyInput = inputStack.isEmpty
        val recipe = SmelterRecipes.getRecipeFromInput(inputStack)
        val furnaceRecipe = FurnaceRecipes.instance().getSmeltingResult(inputStack)
        return !emptyInput && (!furnaceRecipe.isEmpty || recipe != null)
    }

    private fun getCookTime(): Int = 200

    private fun smeltItem() {
        if (currentSmeltingItem.isEmpty) return
        val result = SmelterRecipes.getRecipeFromInput(currentSmeltingItem)?.outputStack ?: FurnaceRecipes.instance().getSmeltingResult(currentSmeltingItem)
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