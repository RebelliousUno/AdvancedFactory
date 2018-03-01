package uno.rebellious.advancedfactory.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile
import uno.rebellious.advancedfactory.tile.TileEntityAdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityController

abstract class BlockAdvancedFactory : Block(Material.CIRCUITS) {
    @SideOnly(Side.CLIENT)
    fun initModel() {
        AdvancedFactory.logger?.log(Level.INFO, "Init Model Registry Name is " + registryName)
        ModelLoader.setCustomModelResourceLocation(
            Item.getItemFromBlock(this),
            0,
            ModelResourceLocation(registryName, "inventory")
        )
    }

    protected fun getFactoryAt(worldIn: World, pos: BlockPos?): TileEntityController? {
        if (pos == null) return null
        val tileEntity = worldIn.getTileEntity(pos)
        return when (tileEntity) {
            is TileEntityController -> tileEntity
            is TileEntityAdvancedFactory -> tileEntity.controllerTile
            else -> null
        }
    }

    override fun breakBlock(worldIn: World?, pos: BlockPos?, state: IBlockState?) {
        AdvancedFactory.logger?.log(Level.INFO, "breakBlock")
        if (worldIn != null && pos != null) {
            AdvancedFactory.logger?.log(Level.INFO, "world and pos not null")
            var tile = worldIn.getTileEntity(pos)
            if (tile != null && tile is TileEntityAdvancedFactory) {
                AdvancedFactory.logger?.log(Level.INFO, "$tile")
                AdvancedFactory.logger?.log(Level.INFO, "${tile.controllerTile}")
                tile.controllerTile?.checkNeighbours(true)
            }
        }
        super.breakBlock(worldIn, pos, state)
    }
}