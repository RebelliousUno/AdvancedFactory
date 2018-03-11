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

    fun drawBox(area: Pair<Pair<Int, Int>, Pair<Int, Int>>, color: Int) {
        drawBox(area.first, area.second, color)
    }

    fun drawBox(first: Pair<Int, Int>, second: Pair<Int, Int>, color: Int) {
        drawBox(first.first, first.second, second.first, second.second, color)
    }

    fun drawBox(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        drawHorizontalLine(x1, x2, y1, color)
        drawHorizontalLine(x1, x2, y2, color)
        drawVerticalLine(x1, y1, y2, color)
        drawVerticalLine(x2, y1, y2, color)
    }
}


