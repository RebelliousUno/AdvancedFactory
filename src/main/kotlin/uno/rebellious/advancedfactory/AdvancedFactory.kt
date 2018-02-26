package uno.rebellious.advancedfactory

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import uno.rebellious.advancedfactory.proxy.CommonProxy
import uno.rebellious.advancedfactory.recipe.CrusherRecipes
import uno.rebellious.advancedfactory.recipe.SmelterRecipes

@Mod(
    modid = AdvancedFactory.MOD_ID,
    name = AdvancedFactory.NAME,
    version = AdvancedFactory.VERSION,
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter"
)
object AdvancedFactory {
    const val MOD_ID = "advancedfactory"
    const val NAME = "AdvancedFactory"
    const val VERSION = "0.0.1"

    @Mod.Instance
    var instance: AdvancedFactory? = null

    @SidedProxy(
        clientSide = "uno.rebellious.advancedfactory.proxy.ClientProxy",
        serverSide = "uno.rebellious.advancedfactory.proxy.ServerProxy",
        modId = MOD_ID
    )
    var proxy: CommonProxy? = null

    var logger: Logger? = null

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy?.preInit(event)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy?.postInit(event)
        CrusherRecipes.initCrusherRecipes()
        SmelterRecipes.initSmelterRecipes()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy?.init(event)
    }
}