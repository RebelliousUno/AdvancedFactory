package uno.rebellious.advancedfactory.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile
import uno.rebellious.advancedfactory.tile.TileEntityController

abstract class BlockAdvancedFactory: Block(Material.CIRCUITS) {


    protected fun getFactoryAt(worldIn: World, pos: BlockPos?): TileEntityController? {
        if (pos == null) return null
        val tileEntity = worldIn.getTileEntity(pos)
        return when (tileEntity) {
            is TileEntityController -> tileEntity
            is IAdvancedFactoryTile -> tileEntity.controllerTile
            else -> null
        }
    }
}