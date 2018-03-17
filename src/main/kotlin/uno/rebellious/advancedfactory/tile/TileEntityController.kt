package uno.rebellious.advancedfactory.tile


import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ITickable
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import org.apache.logging.log4j.Level
import uno.rebellious.advancedfactory.AdvancedFactory
import uno.rebellious.advancedfactory.networking.FactoryProgramMessage
import uno.rebellious.advancedfactory.networking.NetworkHandler
import uno.rebellious.advancedfactory.util.Types

class TileEntityController : TileEntityAdvancedFactory(), ITickable {
    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(2, ItemStack.EMPTY)
    override val factoryBlockType = Types.CONTROLLER
    override var inputStack: ItemStack = itemInventory[0]
    override var outputStack: ItemStack = itemInventory[1]


    override fun update() {
        this.checkNeighbours()
        //makeBasicProgram()
        executeProgram()
    }

    val factoryContents = HashMap<BlockPos, Types>()
    val factoryProgram = ArrayList<Pair<BlockPos, BlockPos>>()


    fun updateFactoryProgram(program: ArrayList<Pair<BlockPos, BlockPos>>) {
        factoryProgram.clear()
        factoryProgram.addAll(program)
        if (!world.isRemote) {
            //NetworkHandler.INSTANCE.sendToServer(FactoryProgramMessage(factoryProgram))
        }
    }

//    override fun readFromNBT(compound: NBTTagCompound) {
//        super.readFromNBT(compound)
//    }
//
//    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
//        var tagCompound = NBTTagCompound()
//        //tagCompound.setTag("inventory", itemInventory)
//        return super.writeToNBT(compound)
//    }

    private fun executeProgram() {
        factoryProgram.forEach {
            //Check first has a stack
            var firstTile = world.getTileEntity(it.first) as IAdvancedFactoryTile
            var secondTile = world.getTileEntity(it.second) as IAdvancedFactoryTile

            if (!firstTile.outputStack.isEmpty) {
                //Check second is empty
                if (secondTile.inputStack.isEmpty) {
                    secondTile.inputStack = firstTile.itemInventory[1].copy()
                    firstTile.outputStack = ItemStack.EMPTY
                } else if (secondTile.inputStack.item == firstTile.outputStack.item) {
                    // same item check if space
                    val inputSpace = secondTile.inputStack.maxStackSize - secondTile.inputStack.count
                    val outputSize = firstTile.outputStack.count

                    if (inputSpace >= outputSize) {
                        secondTile.inputStack.grow(outputSize)
                        firstTile.outputStack = ItemStack.EMPTY
                    } else {
                        secondTile.inputStack.grow(inputSpace)
                        firstTile.outputStack.shrink(inputSpace)
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
            factoryProgram += Pair(anInputHatch!!.pos, aCrusher!!.pos)
            factoryProgram += Pair(aCrusher!!.pos, aSmelter!!.pos)
            factoryProgram += Pair(aSmelter!!.pos, anOutputHatch!!.pos)
            factoryProgram += Pair(anOutputHatch!!.pos, anInputHatch!!.pos)
            //factoryProgram += Pair(anInputHatch!!, aCrusher!!)
            factoryProgram += Pair(aCrusher!!.pos, aSmelter!!.pos)
            factoryProgram += Pair(aSmelter!!.pos, anOutputHatch!!.pos)
            factoryProgram += Pair(anOutputHatch!!.pos, anInputHatch!!.pos)
            factoryProgram += Pair(anInputHatch!!.pos, aCrusher!!.pos)
            //factoryProgram += Pair(aCrusher!!, aSmelter!!)
            factoryProgram += Pair(aSmelter!!.pos, anOutputHatch!!.pos)
            factoryProgram += Pair(anOutputHatch!!.pos, anInputHatch!!.pos)
            factoryProgram += Pair(anInputHatch!!.pos, aCrusher!!.pos)
            factoryProgram += Pair(aCrusher!!.pos, aSmelter!!.pos)
            factoryProgram += Pair(aSmelter!!.pos, anOutputHatch!!.pos)
        }
        //AdvancedFactory.logger?.log(Level.INFO, factoryProgram)
    }

    fun checkNeighbours(recheckMultiblock: Boolean = false) {
        if (recheckMultiblock) {
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
        AdvancedFactory.logger?.log(Level.INFO, factoryProgram)

    }
}



