package uno.rebellious.advancedfactory.item

import net.minecraftforge.oredict.OreDictionary

object Items {
    val ironOreDust = ItemDust("dustIron")
    val goldOreDust = ItemDust("dustGold")

    fun getItemList(): Array<ItemBase> {
        return arrayOf(ironOreDust, goldOreDust)
    }

    fun initModels() {
        getItemList().forEach {
            it.initModel()
        }
    }

    fun registerOres() {
        getItemList().forEach {
            OreDictionary.registerOre(it.name, it)
        }
    }

}