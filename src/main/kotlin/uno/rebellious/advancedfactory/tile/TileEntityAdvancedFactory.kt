package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import uno.rebellious.advancedfactory.util.Types

abstract class TileEntityAdvancedFactory : TileEntity(), IAdvancedFactoryTile {
    abstract var controllerTile: TileEntityController?

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