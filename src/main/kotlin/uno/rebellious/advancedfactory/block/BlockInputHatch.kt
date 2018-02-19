package uno.rebellious.advancedfactory.block

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityInputHatch

class BlockInputHatch : BlockAdvancedFactory(), ITileEntityProvider {

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TileEntityInputHatch()
    }

    init {
        unlocalizedName = "inputhatch"
        setRegistryName("inputhatch")
        setCreativeTab(CreativeTabs.MISC)
        AdvancedFactory.logger?.log(Level.INFO, "Init Registry Name is " + registryName)
    }

    fun initModel() {
        AdvancedFactory.logger?.log(Level.INFO, "Init Model Registry Name is " + registryName)
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, ModelResourceLocation(registryName, "inventory"))
    }

    override fun onBlockActivated(
        worldIn: World?,
        pos: BlockPos?,
        state: IBlockState?,
        playerIn: EntityPlayer?,
        hand: EnumHand?,
        facing: EnumFacing?,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {
        if (worldIn == null || worldIn.isRemote) return true
        this.getFactoryAt(worldIn, pos)?.listBlocks()
        return true
    }
}