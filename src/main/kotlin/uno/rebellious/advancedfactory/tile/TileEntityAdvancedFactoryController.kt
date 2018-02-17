package uno.rebellious.advancedfactory.tile


import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory

class TileEntityAdvancedFactoryController : TileEntity(), ITickable, IAdvancedFactoryTile {
    override fun update() {
        this.factoryContents.add(this.pos)
    }

    private val factoryContents = HashSet<BlockPos>() //TODO: Block or tile?


    override var controllerTile
        get() = this
        set(value) {}

    fun listBlocks() {
        AdvancedFactory.logger?.log(Level.INFO, factoryContents)
    }

}



