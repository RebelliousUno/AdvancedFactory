package uno.rebellious.advancedfactory.networking

import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class FactoryContentsMessage(var controllerPos: BlockPos?) : IMessage {
    constructor() : this(null)

    override fun fromBytes(buf: ByteBuf) {
        controllerPos = BlockPos.fromLong(buf.readLong())
    }

    override fun toBytes(buf: ByteBuf) {
        var pos = controllerPos
        if (pos != null)
            buf.writeLong(pos.toLong())
    }
}