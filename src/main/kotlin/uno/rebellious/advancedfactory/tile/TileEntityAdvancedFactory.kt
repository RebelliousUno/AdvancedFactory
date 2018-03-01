package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.util.Types

abstract class TileEntityAdvancedFactory : TileEntity() {
    abstract var controllerTile: TileEntityController?
    abstract val factoryBlockType: Types


    fun checkNeighbours(multiblockContents: HashMap<BlockPos, Types>, controller: TileEntityController, checkedList: HashSet<BlockPos>) {
        AdvancedFactory.logger?.log(Level.INFO, "checkNeighbours $multiblockContents")
        val neighbours = arrayOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())
        neighbours
            .filter{ !checkedList.contains(it) } // Neighbours not already checked
            //.filter{ !multiblockContents.containsKey(it) } //Neighbours not already in list
            .forEach {
                checkedList += it // First thing...add to checkedList...otherwise it could stay forever
                val mysteryTile = this.world.getTileEntity(it)// Tile must not be an advanced Factory tile
                when {
                    mysteryTile==controller -> {
                        // Tile is the controller
                        // Do nothing
                        // Add to checked
                    }
                    mysteryTile is TileEntityController -> {
                        // Tile is another controller
                        // make sure it's not in the list
                        multiblockContents.remove(it)
                    }
                    mysteryTile is TileEntityAdvancedFactory &&
                            (mysteryTile.controllerTile == null) -> {
                        // Tile is an AdvancedFactory and doesn't have a controller
                        // add it to the multiblock
                        multiblockContents[it] = mysteryTile.factoryBlockType
                        // set its controller
                        mysteryTile.controllerTile = controller
                        // call its checkNeighbours
                        mysteryTile.checkNeighbours(multiblockContents, controller, checkedList)
                    }
                    mysteryTile is TileEntityAdvancedFactory -> {
                        // Tile is an Advanced Factory Tile and has a controller
                        // Make sure it's not in our multiblock
                        if (mysteryTile.controllerTile != controller)
                            multiblockContents.remove(it)
                    }
                    else -> {
                        // Tile must not be an advanced Factory tile
                        // So remove it from the list
                        multiblockContents.remove(it)
                    }
                }
            }
    }
}

/*private fun checkNeighbours() {
        //Get each neighbour,
        //For each neighbour that's not in the list tell it to get it's neighbours (pass it the list, and the controller)
        val pos = this.pos
        val neighbours = arrayOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())
        neighbours.forEach { block ->
            val mysteryTile = this.world.getTileEntity(block)
            when (mysteryTile) {
                is TileEntityController -> //Adjacent block is another controller...
                    // make sure it's not in our list
                    factoryContents.remove(block)
                is TileEntityAdvancedFactory -> // Adjacent block is an AdvancedFactory
                    // If it doesn't have a controller set it to be ours, and add it to the list
                    when {
                        mysteryTile.controllerTile == null -> {
                            mysteryTile.controllerTile = this
                            factoryContents.put(block, mysteryTile.factoryBlockType)
                        }
                        mysteryTile.controllerTile == this -> {
                            // If it is us then make sure it's in our list
                            factoryContents.put(block, mysteryTile.factoryBlockType)
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
    }*/