package uno.rebellious.advancedfactory.block

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock

object Blocks {
    val controller = BlockController()
    val inputHatch = BlockInputHatch()
    val outputHatch = BlockOutputHatch()
    val smelter = BlockSmelter()

    fun initModels() {
        controller.initModel()
        inputHatch.initModel()
        outputHatch.initModel()
        smelter.initModel()
    }

    fun getBlockList(): Array<Block> {
        return arrayOf(controller, inputHatch, outputHatch, smelter)
    }

    fun getItemList(): Array<Item> {
        return arrayOf(
            ItemBlock(controller).setRegistryName(Blocks.controller.registryName),
            ItemBlock(inputHatch).setRegistryName(Blocks.inputHatch.registryName),
            ItemBlock(outputHatch).setRegistryName(Blocks.outputHatch.registryName),
            ItemBlock(smelter).setRegistryName(Blocks.smelter.registryName)

        )

    }

}