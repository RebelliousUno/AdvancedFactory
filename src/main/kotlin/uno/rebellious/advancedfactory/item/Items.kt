package uno.rebellious.advancedfactory.item

object Items {
    val ironOreDust = ItemDust("dustIron")

    fun registerItems() {
        ironOreDust.initModel()
    }

}