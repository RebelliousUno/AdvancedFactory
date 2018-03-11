package uno.rebellious.advancedfactory.gui

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.block.Blocks
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile
import uno.rebellious.advancedfactory.tile.TileEntityController
import uno.rebellious.advancedfactory.util.Types
import java.lang.Math.max


class GuiController(val tile: TileEntityController?, val world: World) : GuiBase() {
    private enum class ControllerButtons(val buttonText: String) {
        NEXT("Next"), PREV("Prev"), ADD_LINK("Add Link")
    }

    private var xSize: Int = 176
    private var ySize: Int = 138

    private var guiLeft: Int = 0
    private var guiTop: Int = 0

    private var buttons = mutableListOf<GuiButton>()
    private var blockList = ArrayList<Triple<Types, Int, Int>>()
    private var pageNo = 0
    private var totalPages = 0

    private var blockClickArea = HashMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, IAdvancedFactoryTile?>()
    private var selectedTile: Pair<Pair<Int, Int>, Pair<Int, Int>>? = null


    override fun initGui() {
        AdvancedFactory.logger?.log(Level.INFO, "initGUI")
        super.initGui()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
        makeButtonList()
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
        val buttonHeight = 20
        val padding = 5
        val buttonBottom = guiTop + ySize - buttonHeight - padding
        val buttonWidth = 10 + max(
            fontRenderer.getStringWidth(ControllerButtons.NEXT.buttonText),
            fontRenderer.getStringWidth(ControllerButtons.PREV.buttonText)
        )

        val leftButtonX = guiLeft + padding
        val rightButtonX = guiLeft + xSize - buttonWidth - padding

        val prevButton =
            GuiButton(ControllerButtons.PREV.ordinal, leftButtonX, buttonBottom, ControllerButtons.PREV.buttonText)
        prevButton.setWidth(buttonWidth)
        val nextButton =
            GuiButton(ControllerButtons.NEXT.ordinal, rightButtonX, buttonBottom, ControllerButtons.NEXT.buttonText)
        nextButton.setWidth(buttonWidth)
        val addLinkButton = GuiButton(ControllerButtons.ADD_LINK.ordinal, 0, 0, ControllerButtons.ADD_LINK.buttonText)
        addLinkButton.setWidth(fontRenderer.getStringWidth(ControllerButtons.ADD_LINK.buttonText) + 10)

        buttonList.add(prevButton)
        buttonList.add(nextButton)
        // buttonList.add(addLinkButton)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        super.mouseClicked(mouseX, mouseY, mouseButton)
        val clickArea = blockClickArea.filter {
            val x1 = it.key.first.first
            val y1 = it.key.first.second
            val x2 = it.key.second.first
            val y2 = it.key.second.second
            mouseX > x1 && mouseX < x2 && mouseY > y1 && mouseY < y2
        }.asSequence().firstOrNull()
        if (clickArea != null) {
            selectedTile = if (selectedTile == clickArea.key)
                null
            else
                clickArea.key
            GuiButton(-1, 0, 0, "").playPressSound(mc.soundHandler)
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawScreenPre(mouseX, mouseY, partialTicks)
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenPost(mouseX, mouseY, partialTicks)
    }

    private fun drawScreenPost(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawProgram()
        pageText()
        drawFactoryContents()
        drawSelected()
    }

    private fun drawSelected() {
        val selection = selectedTile ?: return
        drawBox(selection, 0)
    }

    private fun drawFactoryContents() {
        val x = 0
        var y = 0
        val padding = 3
        tile?.factoryContents?.forEach {
            drawItemStack(ItemStack(Blocks.getBlockFromType(it.value)), x + padding, y + padding, "")
            blockClickArea[Pair(Pair(x, y), Pair(x + 21, y + 21))] =
                    world.getTileEntity(it.key) as? IAdvancedFactoryTile
            y += (21)
        } ?: return
    }

    private fun drawProgram() {
        var x = guiLeft + 10
        var y = guiTop + 10
        val horizontalGap = 28
        val verticalGap = 28
        var slotCount = 1
        var rowCount = 0
        val flow = convertProgramToFlows(tile?.factoryProgram)

        assert(flow.isNotEmpty())

        flow[pageNo].forEach {
            val stack = Blocks.getBlockFromType(it.factoryBlockType)
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

    private fun convertFlowsToProgram(flows: ArrayList<ArrayList<IAdvancedFactoryTile>>): ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>> {
        val program: ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>> = ArrayList()

        flows.forEach { flow ->
            flow.indices.forEach { index ->
                if (index != 0) {
                    program.add(Pair(flow[index - 1], flow[index]))
                }
            }
        }
        return program
    }

    private fun convertProgramToFlows(program: ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>>?): ArrayList<ArrayList<IAdvancedFactoryTile>> {
        val flow = ArrayList<ArrayList<IAdvancedFactoryTile>>()

        var currentFlow = ArrayList<IAdvancedFactoryTile>()
        flow.add(currentFlow)

        program?.forEach {
            if (currentFlow.isEmpty()) {
                currentFlow.add(it.first)
            }
            if (currentFlow.last() != it.first) {
                currentFlow = ArrayList()
                flow.add(currentFlow)
                currentFlow.add(it.first)
            }
            currentFlow.add(it.second)
        }
        return flow
    }

    private fun drawScreenPre(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        GlStateManager.color(1F, 1F, 1F)
        val res = ResourceLocation(AdvancedFactory.MOD_ID, "textures/gui/gui_controller.png")

        this.mc.textureManager.bindTexture(res)
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize)
    }

    override fun actionPerformed(button: GuiButton?) {
        AdvancedFactory.logger?.log(Level.INFO, button?.id)
        if (button?.id == ControllerButtons.PREV.ordinal && pageNo > 0) {
            pageNo--
        } else if (button?.id == ControllerButtons.NEXT.ordinal && pageNo < totalPages - 1) {
            pageNo++
        } /*else if (button?.id == ControllerButtons.ADD_LINK.ordinal) {
            if (tile != null) {
                val contents = tile.factoryContents
                    .filter { it.value != Types.CONTROLLER }
                    .map { world.getTileEntity(it.key) as? IAdvancedFactoryTile }
                if (contents.size >= 2) {
                    if (contents[0] != null && contents[1] != null)
                        tile.factoryProgram.add(Pair(contents[0]!!, contents[1]!!))
                }
            }
        }*/
        super.actionPerformed(button)
    }
}

