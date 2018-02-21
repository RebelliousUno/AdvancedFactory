package uno.rebellious.advancedfactory.tile

import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.CapabilityItemHandler
import uno.rebellious.advancedfactory.handler.HatchInventoryHandler
import uno.rebellious.advancedfactory.handler.ItemDirection

class TileEntityInputHatch : TileEntityHatch(), ICapabilityProvider {
    override val factoryBlockType: String = "inputHatch"


    override fun update() {
        super.update()
        //if something in the input slot move it to the output slot
        moveItemsFromInputToOutput()
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.controllerTile != null) {
            return true
        }
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        this.controllerTile ?: return super.getCapability(capability, facing)
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return HatchInventoryHandler(
            this,
            ItemDirection.INPUT
        ) as T
        return super.getCapability(capability, facing)
    }

}
