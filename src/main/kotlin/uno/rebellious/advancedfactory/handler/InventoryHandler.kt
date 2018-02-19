package uno.rebellious.advancedfactory.handler

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityController

class InventoryHandler(val controller: TileEntityController, private val direction: ItemDirection): IItemHandler {

    var inventory = controller.inventory

    //Get the stack in slot number slot
    override fun getStackInSlot(slot: Int): ItemStack {
        return if (slot > inventory.size) ItemStack.EMPTY else inventory[slot]
    }

    override fun getSlotLimit(slot: Int): Int = 64

    override fun getSlots(): Int = inventory.size

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (direction == ItemDirection.INPUT) return ItemStack.EMPTY
        if (slot > slots) return ItemStack.EMPTY

        //presumably slot is the slot to pull from
        //amount is the amount to pull
        //simulate is whether or not to adjust our stack

        var ourStack = inventory[slot]
        if (amount < ourStack.count) {
            var newStack = ourStack.copy()
            var returnStack = newStack.splitStack(amount)
            if (!simulate) ourStack.shrink(amount)
            return returnStack
        }
        if (amount >= ourStack.count) {
            if (!simulate) inventory[slot] = ItemStack.EMPTY
            return ourStack
        }
        return ItemStack.EMPTY
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        AdvancedFactory.logger?.log(Level.INFO, direction)
        if (direction == ItemDirection.OUTPUT) return stack
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


