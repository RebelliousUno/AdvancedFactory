package uno.rebellious.advancedfactory.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityController

class GuiHandler : IGuiHandler {
    override fun getClientGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        AdvancedFactory.logger?.log(Level.INFO, "getClientGuiElement")
        var tile = world?.getTileEntity(BlockPos(x, y, z)) as? TileEntityController
        return GuiController(tile)
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        AdvancedFactory.logger?.log(Level.INFO, "getServerGuiElement")
        return null
    }

    enum class GuiTypes {
        CONTROLLER
    }


}