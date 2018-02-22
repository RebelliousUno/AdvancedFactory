package uno.rebellious.advancedfactory.item

import net.minecraft.creativetab.CreativeTabs
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory

class ItemDust(name: String) : ItemBase(name) {
    init {
        unlocalizedName = name
        setRegistryName(name)
        creativeTab = CreativeTabs.MISC
        AdvancedFactory.logger?.log(Level.INFO, "Init Registry Name is " + registryName)
    }
}