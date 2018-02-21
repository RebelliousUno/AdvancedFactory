package uno.rebellious.advancedfactory.handler

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import uno.rebellious.advancedfactory.tile.TileEntityHatch

class HatchInventoryHandler(var tile: TileEntityHatch, private val direction: ItemDirection) : IItemHandler {

    var inventory = tile.itemInventory
    //Get the stack in slot number slot
    override fun getStackInSlot(slot: Int): ItemStack {
        return if (slot > inventory.size) ItemStack.EMPTY else inventory[slot]
    }

    override fun getSlotLimit(slot: Int): Int = 64

    override fun getSlots(): Int = 1 //Only returns a specific slot or allows access to a specific slot

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (direction == ItemDirection.INPUT) return ItemStack.EMPTY
        if (slot > slots) return ItemStack.EMPTY
        val extractSlot = 1
        //presumably slot is the slot to pull from
        //amount is the amount to pull
        //simulate is whether or not to adjust our stack

        var ourStack = inventory[extractSlot]
        if (amount < ourStack.count) {
            var newStack = ourStack.copy()
            var returnStack = newStack.splitStack(amount)
            if (!simulate) ourStack.shrink(amount)
            return returnStack
        }
        if (amount >= ourStack.count) {
            if (!simulate) inventory[extractSlot] = ItemStack.EMPTY
            return ourStack
        }
        return ItemStack.EMPTY
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (direction == ItemDirection.OUTPUT) return stack
        if (stack.isEmpty) return ItemStack.EMPTY
        val insertSlot = 0
        val stackInSlot = inventory[insertSlot]

        if (!stackInSlot.isEmpty) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                return stack
            }

            val spaceInSlot = Math.min(stack.maxStackSize, getSlotLimit(insertSlot)) - stackInSlot.count

            if (spaceInSlot >= stack.count) {
                if (!simulate) stackInSlot.grow(stack.count)
                tile.markDirty()
                return ItemStack.EMPTY
            }
            if (spaceInSlot < stack.count) {
                val stackToAdd = stack.copy()
                val newStack = stackToAdd.splitStack(spaceInSlot)
                if (!simulate) {
                    stackInSlot.grow(newStack.count)
                }
                tile.markDirty()
                return stackToAdd
            }
        }
        if (!simulate) inventory[insertSlot] = stack.copy()
        tile.markDirty()
        return ItemStack.EMPTY
    }

}


