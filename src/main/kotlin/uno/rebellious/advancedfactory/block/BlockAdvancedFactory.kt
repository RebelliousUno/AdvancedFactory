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
import uno.rebellious.advancedfactory.tile.TileEntityAdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityController

abstract class BlockAdvancedFactory : Block(Material.CIRCUITS) {
    @SideOnly(Side.CLIENT)
    fun initModel() {
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
            is TileEntityAdvancedFactory -> worldIn.getTileEntity(tileEntity.controllerTile) as? TileEntityController
            else -> null
        }
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        var tile = worldIn.getTileEntity(pos)
        if (tile is TileEntityAdvancedFactory && tile.controllerTile != null) {
            var controller = worldIn.getTileEntity(tile.controllerTile!!)
            if (controller is TileEntityController) {
                super.breakBlock(worldIn, pos, state)
                controller.checkNeighbours(true)
                return
            }
        }
        super.breakBlock(worldIn, pos, state)
    }
}