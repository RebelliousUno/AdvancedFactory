package uno.rebellious.advancedfactory.proxy

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.block.Blocks
import uno.rebellious.advancedfactory.config.GeneralConfig
import java.io.File

@Mod.EventBusSubscriber
open class CommonProxy {
    companion object {
        var config: Configuration? = null

        @JvmStatic
        @SubscribeEvent
        fun registerBlocks(event: RegistryEvent.Register<Block>) {
            //TODO: registerBlocks
            AdvancedFactory.logger?.log(Level.INFO, "Registering Blocks")
            event.registry.register(Blocks.controller)
            //TODO: GameRegistry.registerTileEntity()
        }

        @JvmStatic
        @SubscribeEvent
        fun registerItems(event: RegistryEvent.Register<Item>) {
            //TODO: registerItems
        }
    }

        fun preInit(event: FMLPreInitializationEvent) {
            val configDir = event.modConfigurationDirectory
            config = Configuration(File(configDir.path, "advancedFactory.cfg"))
            try {
                config!!.load()
                GeneralConfig.readConfig(config!!)
            } catch (exception: Exception) {
                AdvancedFactory.logger?.log(Level.ERROR, "Error loading config file", exception)
            } finally {
                if (config!!.hasChanged()) {
                    config!!.save()
                }
            }
        }

        fun init(event: FMLInitializationEvent) {
            //TODO: Init
        }

        fun postInit(event: FMLPostInitializationEvent) {
            if (config?.hasChanged()!!) config?.save()
        }
}

@Mod.EventBusSubscriber
class ServerProxy : CommonProxy()


@Mod.EventBusSubscriber
class ClientProxy : CommonProxy() {
    companion object {
        @JvmStatic
        @SubscribeEvent
        fun registerModels(event: ModelRegistryEvent) {
            //TODO: Register Models
        }
    }
}