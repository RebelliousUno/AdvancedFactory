package uno.rebellious.advancedfactory.block

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityOutputHatch

class BlockOutputHatch : BlockAdvancedFactory(), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TileEntityOutputHatch()
    }

    init {
        unlocalizedName = "outputhatch"
        setRegistryName("outputhatch")
        setCreativeTab(CreativeTabs.MISC)
        AdvancedFactory.logger?.log(Level.INFO, "Init Registry Name is " + registryName)
    }

//    fun initModel() {
//        AdvancedFactory.logger?.log(Level.INFO, "Init Model Registry Name is " + registryName)
//        ModelLoader.setCustomModelResourceLocation(
//            Item.getItemFromBlock(this),
//            0,
//            ModelResourceLocation(registryName, "inventory")
//        )
//    }

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