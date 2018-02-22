package uno.rebellious.advancedfactory.item

import net.minecraft.item.Item
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.model.ModelLoader



open class ItemBase(val name: String): Item() {
    fun initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, ModelResourceLocation(registryName, "inventory"))
    }
}

