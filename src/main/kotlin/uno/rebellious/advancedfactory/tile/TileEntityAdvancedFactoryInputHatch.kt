package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.block.BlockController

class TileEntityAdvancedFactoryInputHatch : TileEntity(), ITickable, IAdvancedFactoryTile {
    private var _controller: TileEntityAdvancedFactoryController? = null
    private var controllerTilePos: BlockPos? = null
    override var controllerTile: TileEntityAdvancedFactoryController?
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
}
