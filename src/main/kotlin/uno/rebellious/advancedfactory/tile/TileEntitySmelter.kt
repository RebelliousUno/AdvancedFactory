package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos

class TileEntitySmelter : TileEntity(), IAdvancedFactoryTile, ITickable {
    private var _controller: TileEntityController? = null
    private var controllerTilePos: BlockPos? = null
    override var controllerTile: TileEntityController?
        get() = this._controller
        set(value) {
            this._controller = value
            this.controllerTilePos = value?.pos
        }

    override val factoryBlockType: String = "Smelter"

    override fun update() {
        //Do smelting operation
    }

}