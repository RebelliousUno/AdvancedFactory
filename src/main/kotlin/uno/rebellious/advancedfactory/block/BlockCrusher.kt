package uno.rebellious.advancedfactory.block

import net.minecraft.block.ITileEntityProvider
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityCrusher

class BlockCrusher : BlockAdvancedFactory(), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World?, meta: Int) = TileEntityCrusher()

    init {
        unlocalizedName = "crusher"
        setRegistryName("crusher")
        setCreativeTab(CreativeTabs.MISC)
    }
}