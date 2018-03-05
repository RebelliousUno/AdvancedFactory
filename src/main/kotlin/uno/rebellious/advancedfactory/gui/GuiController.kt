package uno.rebellious.advancedfactory.gui

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.block.Blocks
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile
import uno.rebellious.advancedfactory.tile.TileEntityController
import uno.rebellious.advancedfactory.util.Types


class GuiController(val tile: TileEntityController?) : GuiBase() {
    private var xSize: Int = 176
    private var ySize: Int = 131

    private var guiLeft: Int = 0
    private var guiTop: Int = 0

    private var buttons = mutableListOf<GuiButton>()
    private var blockList = ArrayList<Triple<Types, Int, Int>>()

    override fun initGui() {
        AdvancedFactory.logger?.log(Level.INFO, "initGUI")
        super.initGui()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
        //makeButtonList()
    }

    private fun makeButtonList() {
        var i = 0
        val height = fontRenderer.FONT_HEIGHT + 5
        tile?.factoryContents
            ?.filterValues { it != Types.CONTROLLER }
            ?.forEach {
                var name = it.value.unlocalizedName
                var width = fontRenderer.getStringWidth(name) + 10
                buttonList.add(GuiButton(i, guiLeft, i * 20, width, height, name))
                var t = Triple(it.value, guiLeft + width, i * 20)
                blockList.add(t)
                drawItemStack(ItemStack(Blocks.controller), guiLeft + width, i * 20, "")
                i++
            }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawScreenPre(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenPost(mouseX, mouseY, partialTicks)
    }

    private fun drawScreenPost(mouseX: Int, mouseY: Int, partialTicks: Float) {
        GlStateManager.color(1F, 1F, 1F)
        var res = ResourceLocation(AdvancedFactory.MOD_ID, "textures/gui/gui_controller.png")

        this.mc.textureManager.bindTexture(res)
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize)
        drawProgram()
    }

    private fun drawProgram() {
        var x = guiLeft + 10
        var y = guiTop + 10
        var slotCount = 1
        var horizontalGap = 28
        var verticalGap = 28
        var rowCount = 0

        var flow = ArrayList<IAdvancedFactoryTile>()

        tile?.factoryProgram?.forEach {
            if (flow.isEmpty()) flow.add(it.first)
            if (flow.last() == it.first) flow.add(it.second)
        }
        flow.forEach {
            var stack = Blocks.getBlockFromType(it.factoryBlockType)
            drawItemStack(ItemStack(stack), x, y, "")
            slotCount++
            when (slotCount) {
                in 1..6, in 8..12, in 14..18, in 20..24 -> {
                    if (rowCount % 2 == 0)
                        x += horizontalGap
                    else
                        x -= horizontalGap
                }
                7, 13, 19 -> {
                    rowCount++
                }
            }
            y = guiTop + 10 + (rowCount * verticalGap)
        }
    }

    private fun drawScreenPre(mouseX: Int, mouseY: Int, partialTicks: Float) {

    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        AdvancedFactory.logger?.log(Level.INFO, "$mouseX, $mouseY, $mouseButton")
    }
}

