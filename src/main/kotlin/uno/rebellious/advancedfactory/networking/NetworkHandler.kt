package uno.rebellious.advancedfactory.networking

import uno.rebellious.advancedfactory.AdvancedFactory
import net.minecraftforge.fml.common.network.NetworkRegistry

object NetworkHandler {
    val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AdvancedFactory.MOD_ID)
}