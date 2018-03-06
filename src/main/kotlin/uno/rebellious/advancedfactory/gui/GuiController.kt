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
import java.lang.Math.max


class GuiController(val tile: TileEntityController?) : GuiBase() {
    private var xSize: Int = 176
    private var ySize: Int = 138

    private var guiLeft: Int = 0
    private var guiTop: Int = 0

    private var buttons = mutableListOf<GuiButton>()
    private var blockList = ArrayList<Triple<Types, Int, Int>>()
    private var pageNo = 0
    private var totalPages = 0

    override fun initGui() {
        AdvancedFactory.logger?.log(Level.INFO, "initGUI")
        super.initGui()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
    }

    private fun pageText() {
        val text = "Page ${pageNo + 1} of $totalPages"
        val textWidth = fontRenderer.getStringWidth(text)
        val padding = 10
        val textBottom = guiTop + ySize - fontRenderer.FONT_HEIGHT - padding
        val textLeft = (guiLeft + (xSize / 2)) - (textWidth / 2)

        fontRenderer.drawString(text, textLeft, textBottom, 4210752)
    }

    private fun makeButtonList() {
        var i = 0
        val buttonHeight = 20
        val padding = 5
        val buttonBottom = guiTop + ySize - buttonHeight - padding
        val buttonWidth = max(fontRenderer.getStringWidth("Next"), fontRenderer.getStringWidth("Prev")) + 10

        val leftButtonX = guiLeft + padding
        val rightButtonX = guiLeft + xSize - buttonWidth - padding


        val prevButton = GuiButton(i, leftButtonX, buttonBottom, "Prev")
        prevButton.setWidth(buttonWidth)
        i++
        val nextButton = GuiButton(i, rightButtonX, buttonBottom, "Next")
        nextButton.setWidth(buttonWidth)

        buttonList.add(prevButton)
        buttonList.add(nextButton)

//        tile?.factoryContents
//            ?.filterValues { it != Types.CONTROLLER }
//            ?.forEach {
//                var name = it.value.unlocalizedName
//                var width = fontRenderer.getStringWidth(name) + 10
//                buttonList.add(GuiButton(i, guiLeft, i * 20, width, height, name))
//                var t = Triple(it.value, guiLeft + width, i * 20)
//                blockList.add(t)
//                drawItemStack(ItemStack(Blocks.controller), guiLeft + width, i * 20, "")
//                i++
//            }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawScreenPre(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenPost(mouseX, mouseY, partialTicks)
    }

    private fun drawScreenPost(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawProgram()
        makeButtonList()
        pageText()
    }

    private fun drawProgram() {
        var x = guiLeft + 10
        var y = guiTop + 10
        var slotCount = 1
        var horizontalGap = 28
        var verticalGap = 28
        var rowCount = 0

        var flow = ArrayList<ArrayList<IAdvancedFactoryTile>>()

        var currentFlow = ArrayList<IAdvancedFactoryTile>()
        flow.add(currentFlow)

        tile?.factoryProgram?.forEach {
            if (currentFlow.isEmpty())
                currentFlow.add(it.first)
            if (currentFlow.last() != it.first) {
                currentFlow = ArrayList()
                flow.add(currentFlow)
            }
            currentFlow.add(it.second)

        }

        assert(flow.isNotEmpty())

        flow[0].forEach {
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
        totalPages = flow.size
    }

    private fun drawScreenPre(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        GlStateManager.color(1F, 1F, 1F)
        var res = ResourceLocation(AdvancedFactory.MOD_ID, "textures/gui/gui_controller.png")

        this.mc.textureManager.bindTexture(res)
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        AdvancedFactory.logger?.log(Level.INFO, "$mouseX, $mouseY, $mouseButton")
    }
}

