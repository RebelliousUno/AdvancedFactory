package uno.rebellious.advancedfactory.block

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock

object Blocks {
    val controller = BlockController()
    val inputHatch = BlockInputHatch()
    val outputHatch = BlockOutputHatch()

    fun initModels() {
        controller.initModel()
        inputHatch.initModel()
        outputHatch.initModel()
    }
    fun getBlockList(): Array<Block> {
        return arrayOf(controller, inputHatch, outputHatch)
    }
    fun getItemList(): Array<Item> {
        return arrayOf(
            ItemBlock(controller).setRegistryName(Blocks.controller.registryName),
            ItemBlock(inputHatch).setRegistryName(Blocks.inputHatch.registryName),
            ItemBlock(outputHatch).setRegistryName(Blocks.outputHatch.registryName)
        )

    }
//TODO Implement    val itemInputHatch = BlockInputHatch<Item>()
//TODO Implement    val fluidInputHatch = BlockInputHatch<Fluid>()
//TODO Implement    val outputHatch = BlockOutputHatch<Item>()
//TODO Implement    val powerInputHatch = BlockPowerInputHatch()

}