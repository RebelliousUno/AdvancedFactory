package uno.rebellious.advancedfactory.networking

import net.minecraftforge.fml.common.network.NetworkRegistry
import uno.rebellious.advancedfactory.AdvancedFactory

object NetworkHandler {
    val INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(AdvancedFactory.MOD_ID)
}