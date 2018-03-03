package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.util.Types

abstract class TileEntityAdvancedFactory : TileEntity() {
    abstract var controllerTile: TileEntityController?
    abstract val factoryBlockType: Types


    fun checkNeighbours(
        multiblockContents: HashMap<BlockPos, Types>,
        controller: TileEntityController,
        checkedList: HashSet<BlockPos>
    ) {
        val neighbours = arrayOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())
        neighbours
            .filter { !checkedList.contains(it) } // Neighbours not already checked
            .forEach {
                checkedList += it // First thing...add to checkedList...otherwise it could stay forever
                val mysteryTile = this.world.getTileEntity(it)// Tile must not be an advanced Factory tile
                when {
                    mysteryTile is TileEntityController -> {
                        multiblockContents.remove(it)
                    }
                    mysteryTile is TileEntityAdvancedFactory &&
                            (mysteryTile.controllerTile == null) -> {
                        multiblockContents[it] = mysteryTile.factoryBlockType
                        mysteryTile.controllerTile = controller
                        mysteryTile.checkNeighbours(multiblockContents, controller, checkedList)
                    }
                    mysteryTile is TileEntityAdvancedFactory -> {
                        if (mysteryTile.controllerTile != controller)
                            multiblockContents.remove(it)
                        else {// otherwise is already part of our multiblock, so make sure it's in our list
                            // and check its neighbours
                            multiblockContents[it] = mysteryTile.factoryBlockType
                            mysteryTile.checkNeighbours(multiblockContents, controller, checkedList)
                        }
                    }
                    else -> {
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