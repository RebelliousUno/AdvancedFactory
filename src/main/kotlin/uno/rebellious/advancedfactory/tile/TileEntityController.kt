package uno.rebellious.advancedfactory.tile


import net.minecraft.item.ItemStack
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.util.Types

class TileEntityController : TileEntityAdvancedFactory(), ITickable, IAdvancedFactoryTile {
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(2, ItemStack.EMPTY)
    override val factoryBlockType = Types.CONTROLLER
    override var inputStack: ItemStack = itemInventory[0]
    override var outputStack: ItemStack = itemInventory[1]


    override fun update() {
        this.checkNeighbours()
        //makeBasicProgram()
        //executeProgram()
    }

    private val factoryContents = HashMap<BlockPos, Types>()
    private val factoryProgram = ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>>()


    private fun executeProgram() {
        factoryProgram.forEach {
            //Check first has a stack
            if (!it.first.outputStack.isEmpty) {
                //Check second is empty
                if (it.second.inputStack.isEmpty) {
                    it.second.inputStack = it.first.itemInventory[1].copy()
                    it.first.outputStack = ItemStack.EMPTY
                } else if (it.second.inputStack.item == it.first.outputStack.item) {
                    // same item check if space
                    val inputSpace = it.second.inputStack.maxStackSize - it.second.inputStack.count
                    val outputSize = it.first.outputStack.count

                    if (inputSpace >= outputSize) {
                        it.second.inputStack.grow(outputSize)
                        it.first.outputStack = ItemStack.EMPTY
                    } else {
                        it.second.inputStack.grow(inputSpace)
                        it.first.outputStack.shrink(inputSpace)
                    }
                }
            }
        }
    }

    private fun makeBasicProgram() {
        // set up a basic test program
        if (factoryProgram.isNotEmpty()) return

        // find an input hatch in factory blocks
        // find an output hatch in factory blocks
        // add them to the program

        var anInputHatch: TileEntityInputHatch? = null
        var anOutputHatch: TileEntityOutputHatch? = null
        var aSmelter: TileEntitySmelter? = null
        var aCrusher: TileEntityCrusher? = null


        factoryContents.forEach {
            if (it.value == Types.INPUT_HATCH) anInputHatch = world.getTileEntity(it.key) as TileEntityInputHatch
            if (it.value == Types.OUTPUT_HATCH) anOutputHatch = world.getTileEntity(it.key) as TileEntityOutputHatch
            if (it.value == Types.SMELTER) aSmelter = world.getTileEntity(it.key) as TileEntitySmelter
            if (it.value == Types.CRUSHER) aCrusher = world.getTileEntity(it.key) as TileEntityCrusher
        }
        if (anInputHatch != null && anOutputHatch != null && aSmelter != null && aCrusher != null) {
            factoryProgram += Pair(anInputHatch!!, aCrusher!!)
            factoryProgram += Pair(aCrusher!!, aSmelter!!)
            factoryProgram += Pair(aSmelter!!, anOutputHatch!!)
        }
        //AdvancedFactory.logger?.log(Level.INFO, factoryProgram)
    }

    fun checkNeighbours(recheckMultiblock: Boolean = false) {
        if (recheckMultiblock) {
            AdvancedFactory.logger?.log(Level.INFO, "Rechecking Multiblock")
            factoryContents.keys.forEach {
                var mysteryTile = this.world.getTileEntity(it)
                if (mysteryTile is TileEntityAdvancedFactory) mysteryTile.controllerTile = null
            }
            factoryContents.clear()
            controllerTile = this
        }
        var checkedList = HashSet<BlockPos>()
        checkedList.add(pos)
        factoryContents[pos] = factoryBlockType
        checkNeighbours(factoryContents, this, checkedList)
    }

    override var controllerTile: TileEntityController?
        get() = this
        set(value) {}

    fun listBlocks() {
        AdvancedFactory.logger?.log(Level.INFO, factoryContents)


    }
}



