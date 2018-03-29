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
    val energyInputHatch = BlockEnergyInput()

    fun initModels() {
        controller.initModel()
        inputHatch.initModel()
        outputHatch.initModel()
        smelter.initModel()
        crusher.initModel()
        energyInputHatch.initModel()
    }

    fun getBlockList(): Array<Block> {
        return arrayOf(controller, inputHatch, outputHatch, smelter, crusher, energyInputHatch)
    }

    fun getItemList(): Array<Item> {
        return getBlockList()
            .map {
                ItemBlock(it).setRegistryName(it.registryName)
            }.toTypedArray()
    }

    fun getBlockFromType(type: Types): BlockAdvancedFactory {
        return when (type) {
            Types.CONTROLLER -> controller
            Types.INPUT_HATCH -> inputHatch
            Types.OUTPUT_HATCH -> outputHatch
            Types.SMELTER -> smelter
            Types.CRUSHER -> crusher
            Types.ENERGY_INPUT -> energyInputHatch
            Types.ENERGY_STORAGE -> energyInputHatch
        }
    }

}