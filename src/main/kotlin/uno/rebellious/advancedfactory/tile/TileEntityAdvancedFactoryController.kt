package uno.rebellious.advancedfactory.tile


import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory

class TileEntityAdvancedFactoryController : TileEntity(), ITickable, IAdvancedFactoryTile {
    override fun update() {
        this.factoryContents.add(this.pos)
        this.checkNeighbours()
    }

    private val factoryContents = HashSet<BlockPos>() //TODO: Block or tile?

    private fun checkNeighbours() {
        val pos = this.pos
        val neighbours = arrayOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())
        neighbours.forEach { block ->
            val mysteryTile = this.world.getTileEntity(block)
            when (mysteryTile) {
                is TileEntityAdvancedFactoryController -> //Adjacent block is another controller...
                    // make sure it's not in our list
                    factoryContents.remove(block)
                is IAdvancedFactoryTile -> // Adjacent block is an AdvancedFactory
                    // If it doesn't have a controller set it to be ours, and add it to the list
                    when {
                        mysteryTile.controllerTile == null -> {
                            mysteryTile.controllerTile = this
                            factoryContents.add(block)
                        }
                        mysteryTile.controllerTile == this -> {
                            // If it is us then make sure it's in our list
                            factoryContents.add(block)
                        }
                        else -> {
                            // Otherwise it's got a controller that's not us
                            // Make sure it's not in our list
                            factoryContents.remove(block)
                        }
                    }
                else -> // Not an AdvFactoryBlock remove from our list
                    factoryContents.remove(block)
            }
        }
    }

    override var controllerTile: TileEntityAdvancedFactoryController?
        get() = this
        set(value) {}

    fun listBlocks() {
        AdvancedFactory.logger?.log(Level.INFO, factoryContents)
    }

}


