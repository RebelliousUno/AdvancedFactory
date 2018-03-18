package uno.rebellious.advancedfactory.tile

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.CapabilityItemHandler
import uno.rebellious.advancedfactory.handler.HatchInventoryHandler
import uno.rebellious.advancedfactory.handler.ItemDirection
import uno.rebellious.advancedfactory.util.Types

class TileEntityOutputHatch : TileEntityHatch(), ICapabilityProvider {
    override val factoryBlockType = Types.OUTPUT_HATCH

    override fun update() {
        //Check if still has a valid controller
        //If not then remove it
        super.update()
        moveItemsFromInputToOutput()
    }

    private var _controller: BlockPos? = null
    override var controllerTile: BlockPos?
        get() = this._controller
        set(value) {
            this._controller = value
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
            ItemDirection.OUTPUT
        ) as T
        return super.getCapability(capability, facing)
    }
}