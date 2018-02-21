package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.CapabilityItemHandler
import uno.rebellious.advancedfactory.handler.HatchInventoryHandler
import uno.rebellious.advancedfactory.handler.ItemDirection

class TileEntityOutputHatch : TileEntityHatch(), ITickable, ICapabilityProvider, IAdvancedFactoryTile {
    override val factoryBlockType: String = "outputHatch"

    override fun update() {
        //Check if still has a valid controller
        //If not then remove it
        super.update()
        moveItemsFromInputToOutput()
    }

    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
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