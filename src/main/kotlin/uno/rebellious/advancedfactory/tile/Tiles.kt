package uno.rebellious.advancedfactory.tile

import net.minecraft.tileentity.TileEntity
import uno.rebellious.advancedfactory.AdvancedFactory

object Tiles {
    fun getTiles(): Array<Pair<Class<out TileEntity>, String>> = arrayOf(
        Pair<Class<out TileEntity>, String>(TileEntitySmelter::class.java, AdvancedFactory.MOD_ID + ":smelter"),
        Pair<Class<out TileEntity>, String>(TileEntityController::class.java, AdvancedFactory.MOD_ID + ":controller"),
        Pair<Class<out TileEntity>, String>(TileEntityInputHatch::class.java, AdvancedFactory.MOD_ID + ":inputhatch"),
        Pair<Class<out TileEntity>, String>(TileEntityOutputHatch::class.java, AdvancedFactory.MOD_ID + ":outputhatch"),
        Pair<Class<out TileEntity>, String>(TileEntitySmelter::class.java, AdvancedFactory.MOD_ID + ":smelter"),
        Pair<Class<out TileEntity>, String>(TileEntityCrusher::class.java, AdvancedFactory.MOD_ID + ":crusher")
    )
}