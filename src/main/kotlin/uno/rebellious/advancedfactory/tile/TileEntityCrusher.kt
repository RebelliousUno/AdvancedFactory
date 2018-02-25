package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.recipe.CrusherRecipes
import uno.rebellious.advancedfactory.util.Helpers
import uno.rebellious.advancedfactory.util.Types
import java.util.*

class TileEntityCrusher : TileEntity(), IAdvancedFactoryTile, ITickable {
    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(4, ItemStack.EMPTY)

    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override val factoryBlockType = Types.CRUSHER

    private var currentCrushingItem
        get() = itemInventory[2]
        set(value) {
            itemInventory[2] = value
        }
    private var secondaryOutputStack
        get() = itemInventory[3]
        set(value) {
            itemInventory[3] = value //or should this be a copy?
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
            currentCrushingTime = crushTime
        }
        moveItemFromSecondaryOutput()
    }

    private fun moveItemFromSecondaryOutput() {
        //Move item from secondaryOutput to Normal output
        if (!secondaryOutputStack.isEmpty) {
            if (outputStack.isEmpty) {
                outputStack = secondaryOutputStack.copy()
                secondaryOutputStack = ItemStack.EMPTY
            } else {
                if (outputStack.item == secondaryOutputStack.item) {
                    val spaceInOutputStack = Helpers.spaceInStack(outputStack)
                    if (spaceInOutputStack >= secondaryOutputStack.count) {
                        outputStack.grow(secondaryOutputStack.count)
                        secondaryOutputStack = ItemStack.EMPTY
                    } else {
                        secondaryOutputStack.shrink(spaceInOutputStack)
                        outputStack.grow(spaceInOutputStack)
                    }
                }
            }
        }
    }


    private fun moveItemToCrush() {
        val itemToCrush = inputStack.copy()
        itemToCrush.count = 1
        currentCrushingItem = itemToCrush.copy()
        inputStack.shrink(1)
    }

    private fun canCrush(): Boolean {
        //Check there's a recipe for the input item
        return CrusherRecipes.getRecipeFromInput(inputStack) != null
    }

    private val crushTime = 200

    private fun crushItem(): Boolean {
        val recipe = CrusherRecipes.getRecipeFromInput(currentCrushingItem) ?: return false
        val output1 = recipe.outputOne.copy()
        val output2 = recipe.outputTwo.copy()
        val output2Chance = recipe.outputTwoChance

        // 2 outputs outputStack, secondaryOutputStack
        // Check they're both empty first
        return when {
            outputStack.isEmpty && secondaryOutputStack.isEmpty -> {
                // Both Empty so move items
                outputStack = output1.copy()
                if (Random().nextInt(100) < output2Chance) secondaryOutputStack = output2.copy()
                currentCrushingItem = ItemStack.EMPTY
                true
            }
        // If not empty check both are the right type and have space
            output1.item == outputStack.item &&
                    output2.item == secondaryOutputStack.item &&
                    Helpers.spaceInStack(outputStack) >= output1.count &&
                    Helpers.spaceInStack(secondaryOutputStack) >= output2.count -> {
                outputStack.grow(output1.count)
                if (Random().nextInt(100) < output2Chance) secondaryOutputStack.grow(output2.count)
                currentCrushingItem = ItemStack.EMPTY
                true
            }
            else -> false
        }
    }
}
