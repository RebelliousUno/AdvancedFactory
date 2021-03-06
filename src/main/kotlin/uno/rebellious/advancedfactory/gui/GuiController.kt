package uno.rebellious.advancedfactory.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.block.Blocks
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile
import uno.rebellious.advancedfactory.tile.TileEntityAdvancedFactory
import uno.rebellious.advancedfactory.tile.TileEntityController
import uno.rebellious.advancedfactory.util.Types
import java.lang.Math.max


class GuiController(val tile: TileEntityController?, val world: World) : GuiBase() {
    private enum class ControllerButtons(val buttonText: String) {
        NEXT("Next"), PREV("Prev"), ADD_LINK("Add Link"), NEW_FLOW("New Flow");
        val width = Minecraft.getMinecraft().fontRenderer.getStringWidth(buttonText)
    }

    private val logger = AdvancedFactory.logger

    private var xSize: Int = 176
    private var ySize: Int = 138

    private var guiLeft: Int = 0
    private var guiTop: Int = 0

    private var pageNo = 0
    private val totalPages: Int
        get() = flow.size

    private var blockClickArea = HashMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, IAdvancedFactoryTile?>()
    private var selectedTile: Map.Entry<Pair<Pair<Int, Int>, Pair<Int, Int>>, IAdvancedFactoryTile?>? = null

    private lateinit var flow: ArrayList<ArrayList<IAdvancedFactoryTile>>


    override fun initGui() {
        super.initGui()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
        makeButtonList()
        flow = convertProgramToFlows(tile?.factoryProgram)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawScreenPre()
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenPost()
    }

    private fun drawScreenPre() {
        drawDefaultBackground()
        GlStateManager.color(1F, 1F, 1F)
        val res = ResourceLocation(AdvancedFactory.MOD_ID, "textures/gui/gui_controller.png")

        this.mc.textureManager.bindTexture(res)
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize)
    }

    private fun drawScreenPost() {
        drawProgram()
        pageText()
        drawFactoryContents()
        drawSelected()
        drawEnergy()
    }

    private fun drawEnergy() {
        val tileEntityController = tile ?: return
        val storedRatio = tileEntityController.getStoredEnergy().toDouble() / tileEntityController.getMaxEnergy().toDouble()
        val res = ResourceLocation(AdvancedFactory.MOD_ID, "textures/gui/gui_energy_blob.png")
        this.mc.textureManager.bindTexture(res)
        val textureX = 0
        val textureY = 0
        val textureHeight = 4
        val textureWidth = 184
        val energyLeft = 50
        val energyBottom = 50
        drawTexturedModalRect(energyLeft, energyBottom, textureX, textureY, textureWidth, textureHeight)
    }

    private fun drawProgram() {
        var x = guiLeft + 10
        var y = guiTop + 10
        val horizontalGap = 28
        val verticalGap = 28
        var slotCount = 1
        var rowCount = 0

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

    private fun drawSelected() {
        val selection = selectedTile ?: return
        drawBox(selection.key, 0)
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
        val buttonWidth = 10 + max(ControllerButtons.NEXT.width, ControllerButtons.PREV.width)

        val leftButtonX = guiLeft + padding
        val rightButtonX = guiLeft + xSize - buttonWidth - padding

        val prevButton =
            GuiButton(ControllerButtons.PREV.ordinal, leftButtonX, buttonBottom, ControllerButtons.PREV.buttonText)
        prevButton.setWidth(buttonWidth)
        val nextButton =
            GuiButton(ControllerButtons.NEXT.ordinal, rightButtonX, buttonBottom, ControllerButtons.NEXT.buttonText)
        nextButton.setWidth(buttonWidth)
        val addLinkButton = GuiButton(ControllerButtons.ADD_LINK.ordinal, 50, 0, ControllerButtons.ADD_LINK.buttonText)
        addLinkButton.setWidth(ControllerButtons.ADD_LINK.width + 10)

        val newFlowButton = GuiButton(ControllerButtons.NEW_FLOW.ordinal, 50, 20, ControllerButtons.NEW_FLOW.buttonText)
        newFlowButton.setWidth(ControllerButtons.NEW_FLOW.width + 10)
        buttonList.add(prevButton)
        buttonList.add(nextButton)
        buttonList.add(addLinkButton)
        buttonList.add(newFlowButton)
    }

    private fun convertFlowsToProgram(flows: ArrayList<ArrayList<IAdvancedFactoryTile>>): ArrayList<Pair<BlockPos, BlockPos>> {
        val program: ArrayList<Pair<BlockPos, BlockPos>> = ArrayList()

        flows.map {
            it.map {
                it as TileEntityAdvancedFactory
            }
        }
            .forEach { flow ->
                flow.indices.forEach { index ->
                    if (index != 0) {
                        var first = flow[index - 1]
                        var second = flow[index]
                        program.add(Pair(first.pos, second.pos))
                    }
                }
            }
        return program
    }

    private fun convertProgramToFlows(program: ArrayList<Pair<BlockPos, BlockPos>>?): ArrayList<ArrayList<IAdvancedFactoryTile>> {
        val flow = ArrayList<ArrayList<IAdvancedFactoryTile>>()

        var currentFlow = ArrayList<IAdvancedFactoryTile>()
        flow.add(currentFlow)

        program?.map {
            val firstTile = world.getTileEntity(it.first) as? IAdvancedFactoryTile
            val secondTile = world.getTileEntity(it.second) as? IAdvancedFactoryTile
            Pair(firstTile, secondTile)
        }?.forEach {
            var firstTile = it.first
            var secondTile = it.second
            if (firstTile != null && secondTile != null) {
                if (currentFlow.isEmpty()) {
                    currentFlow.add(firstTile)
                }
                if (currentFlow.last() != firstTile) {
                    currentFlow = ArrayList()
                    flow.add(currentFlow)
                    currentFlow.add(firstTile)
                }
                currentFlow.add(secondTile)
            }
        }
        return flow
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
            selectedTile = if (selectedTile == clickArea)
                null
            else
                clickArea
            GuiButton(-1, 0, 0, "").playPressSound(mc.soundHandler)
        }
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.id == ControllerButtons.PREV.ordinal && pageNo > 0) {
            pageNo--
        } else if (button.id == ControllerButtons.NEXT.ordinal && pageNo < totalPages - 1) {
            pageNo++
        } else if (button.id == ControllerButtons.ADD_LINK.ordinal) {
            addLinkAction()
        } else if (button.id == ControllerButtons.NEW_FLOW.ordinal) {
            flow.add(ArrayList())
        }
        super.actionPerformed(button)
    }

    private fun addLinkAction() {
        val selectionToAdd = selectedTile?.value
        if (selectionToAdd != null) {
            val flows = flow
            val currentFlow = flows[pageNo]
            currentFlow.add(selectionToAdd)
            selectedTile = null
            if (currentFlow.size > 1)
                tile?.updateFactoryProgram(convertFlowsToProgram(flows))
        }
    }
}

