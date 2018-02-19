package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.items.CapabilityItemHandler
import uno.rebellious.advancedfactory.block.BlockController
import uno.rebellious.advancedfactory.handler.InventoryHandler
import uno.rebellious.advancedfactory.handler.ItemDirection

class TileEntityInputHatch : TileEntity(), ICapabilityProvider, ITickable, IAdvancedFactoryTile {
    override val factoryBlockType: String = "inputHatch"
    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override fun update() {
        //Check if still has a valid controller
        //If not then remove it
        if (controllerTile != null && controllerTilePos != null) {
            val block = this.world.getBlockState(controllerTilePos!!).block
            if (block !is BlockController) controllerTile = null
        }
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.controllerTile != null) {
            return true
        }
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        val contTile = this.controllerTile ?: return super.getCapability(capability, facing)
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return InventoryHandler(
            contTile,
            ItemDirection.INPUT
        ) as T
        return super.getCapability(capability, facing)
    }

}
