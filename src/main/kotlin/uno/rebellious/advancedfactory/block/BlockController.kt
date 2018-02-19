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
import uno.rebellious.advancedfactory.tile.TileEntityController

class BlockController : BlockAdvancedFactory(), ITileEntityProvider {

    init {
        unlocalizedName = "controller"
        setRegistryName("controller")
        setCreativeTab(CreativeTabs.MISC)
        AdvancedFactory.logger?.log(Level.INFO, "Init Registry Name is " + registryName)
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        AdvancedFactory.logger?.log(Level.INFO, "Creating new tile entity")
        return TileEntityController()
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