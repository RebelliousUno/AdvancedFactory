package uno.rebellious.advancedfactory.networking

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class FactoryProgramMessage(var controller: BlockPos?, var factoryProgram: ArrayList<Pair<BlockPos, BlockPos>>) :
    IMessage {
    constructor() : this(null, ArrayList())

    override fun fromBytes(buf: ByteBuf) {
        controller = BlockPos.fromLong(buf.readLong())
        val size = buf.readInt()
        (1..size).forEach {
            factoryProgram.add(Pair(BlockPos.fromLong(buf.readLong()), BlockPos.fromLong(buf.readLong())))
        }
    }

    override fun toBytes(buf: ByteBuf) {
        val tile = controller
        if (tile != null) {
            buf.writeLong(tile.toLong())
            val size = factoryProgram.size
            buf.writeInt(size)
            factoryProgram.forEach {
                buf.writeLong(it.first.toLong())
                buf.writeLong(it.second.toLong())
            }
        }
    }
}