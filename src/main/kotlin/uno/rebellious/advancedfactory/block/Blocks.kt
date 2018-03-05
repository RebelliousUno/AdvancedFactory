package uno.rebellious.advancedfactory.block

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import uno.rebellious.advancedfactory.util.Types

object Blocks {
    val controller = BlockController()
    val inputHatch = BlockInputHatch()
    val outputHatch = BlockOutputHatch()
    val smelter = BlockSmelter()
    val crusher = BlockCrusher()

    fun initModels() {
        controller.initModel()
        inputHatch.initModel()
        outputHatch.initModel()
        smelter.initModel()
        crusher.initModel()
    }

    fun getBlockList(): Array<Block> {
        return arrayOf(controller, inputHatch, outputHatch, smelter, crusher)
    }

    fun getItemList(): Array<Item> {
        return arrayOf(
            ItemBlock(controller).setRegistryName(Blocks.controller.registryName),
            ItemBlock(inputHatch).setRegistryName(Blocks.inputHatch.registryName),
            ItemBlock(outputHatch).setRegistryName(Blocks.outputHatch.registryName),
            ItemBlock(smelter).setRegistryName(Blocks.smelter.registryName),
            ItemBlock(crusher).setRegistryName(Blocks.crusher.registryName)
        )
    }

    fun getBlockFromType(type: Types): BlockAdvancedFactory {
        return when (type) {
            Types.CONTROLLER -> controller
            Types.INPUT_HATCH -> inputHatch
            Types.OUTPUT_HATCH -> outputHatch
            Types.SMELTER -> smelter
            Types.CRUSHER -> crusher
        }
    }

}