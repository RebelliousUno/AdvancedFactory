package uno.rebellious.advancedfactory.tile


import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory

class TileEntityController : TileEntity(), ITickable, IAdvancedFactoryTile {
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    override val factoryBlockType: String = "controller"

    override fun update() {
        this.factoryContents.put(this.pos, factoryBlockType)
        this.checkNeighbours()
        makeBasicProgram()
        executeProgram()
    }

    private val factoryContents = HashMap<BlockPos, String>() //TODO: Block or tile?
    private val factoryProgram = ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>>()


    private fun executeProgram() {
        factoryProgram.forEach {
            //Check first has a stack
            if (!it.first.itemInventory[1].isEmpty) {
                //Check second is empty
                if (it.second.itemInventory[0].isEmpty) {
                    it.second.itemInventory[0] = it.first.itemInventory[1].copy()
                    it.first.itemInventory[1] = ItemStack.EMPTY
                } else if (it.second.itemInventory[0].item == it.first.itemInventory[1].item) {
                    // same item check if space
                    var inputSpace = it.second.itemInventory[0].maxStackSize - it.second.itemInventory[0].count
                    var outputSize = it.first.itemInventory[1].count

                    if (inputSpace >= outputSize) {
                        it.second.itemInventory[0].grow(outputSize)
                        it.first.itemInventory[1] = ItemStack.EMPTY
                    } else {
                        it.second.itemInventory[0].grow(inputSpace)
                        it.first.itemInventory[1].shrink(inputSpace)
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


        factoryContents.forEach{
            if (it.value == "inputHatch") anInputHatch = world.getTileEntity(it.key) as TileEntityInputHatch
            if (it.value == "outputHatch") anOutputHatch = world.getTileEntity(it.key) as TileEntityOutputHatch
        }
        if (anInputHatch != null && anOutputHatch != null) factoryProgram += Pair(anInputHatch!!,anOutputHatch!!)

        AdvancedFactory.logger?.log(Level.INFO, factoryProgram)
    }

    private fun checkNeighbours() {
        val pos = this.pos
        val neighbours = arrayOf(pos.up(), pos.down(), pos.east(), pos.west(), pos.north(), pos.south())
        neighbours.forEach { block ->
            val mysteryTile = this.world.getTileEntity(block)
            when (mysteryTile) {
                is TileEntityController -> //Adjacent block is another controller...
                    // make sure it's not in our list
                    factoryContents.remove(block)
                is IAdvancedFactoryTile -> // Adjacent block is an AdvancedFactory
                    // If it doesn't have a controller set it to be ours, and add it to the list
                    when {
                        mysteryTile.controllerTile == null -> {
                            mysteryTile.controllerTile = this
                            factoryContents.put(block, mysteryTile.factoryBlockType)
                        }
                        mysteryTile.controllerTile == this -> {
                            // If it is us then make sure it's in our list
                            factoryContents.put(block, mysteryTile.factoryBlockType)
                        }
                        else -> {
                            // Otherwise it's got a controller that's not us
                            // Make sure it's not in our list
                            factoryContents.remove(block)
                        }
                    }
                else -> // Not an AdvFactoryBlock remove from our list
                    factoryContents.remove(block)
            }
        }
    }

    override var controllerTile: TileEntityController?
        get() = this
        set(value) {}

    fun listBlocks() {
        AdvancedFactory.logger?.log(Level.INFO, factoryContents)


    }
}



