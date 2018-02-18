package uno.rebellious.advancedfactory.handler

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityAdvancedFactoryController

class InventoryHandler(val controller: TileEntityAdvancedFactoryController): IItemHandler {

    var inventory = controller.inventory

    //Get the stack in slot number slot
    override fun getStackInSlot(slot: Int): ItemStack {
        return if (slot > inventory.size) ItemStack.EMPTY else inventory[slot]
    }

    override fun getSlotLimit(slot: Int): Int = 64

    override fun getSlots(): Int = inventory.size

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty) return ItemStack.EMPTY
        val stackInSlot = inventory[slot]

        if (!stackInSlot.isEmpty) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                return stack
            }

            val spaceInSlot = Math.min(stack.maxStackSize, getSlotLimit(slot)) - stackInSlot.count

            if (spaceInSlot >= stack.count) {
                if (!simulate) stackInSlot.grow(stack.count)
                controller.markDirty()
                return ItemStack.EMPTY
            }
            if (spaceInSlot < stack.count) {
                val stackToAdd = stack.copy()
                val newStack = stackToAdd.splitStack(spaceInSlot)
                if (!simulate) {
                    stackInSlot.grow(newStack.count)
                }
                controller.markDirty()
                return stackToAdd
            }
        }
        if (!simulate) inventory[slot] = stack.copy()
        controller.markDirty()
        return ItemStack.EMPTY
    }

}


