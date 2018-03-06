package uno.rebellious.advancedfactory.gui

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack

open class GuiBase : GuiScreen() {
    fun drawItemStack(stack: ItemStack, x: Int, y: Int, altText: String) {
        GlStateManager.translate(0.0f, 0.0f, 32.0f)
        this.zLevel = 200.0f
        this.itemRender.zLevel = 200.0f
        var font: net.minecraft.client.gui.FontRenderer? = stack.item.getFontRenderer(stack)
        if (font == null) font = fontRenderer
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y)
        this.itemRender.renderItemOverlayIntoGUI(
            font!!,
            stack,
            x,
            y - 8,
            altText
        )
        this.zLevel = 0.0f
        this.itemRender.zLevel = 0.0f
    }
}


