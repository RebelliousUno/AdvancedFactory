package uno.rebellious.advancedfactory.networking

import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryTile

class FactoryProgramMessage(factoryProgram: ArrayList<Pair<IAdvancedFactoryTile, IAdvancedFactoryTile>>) : IMessage {
    override fun fromBytes(buf: ByteBuf?) {

    }

    override fun toBytes(buf: ByteBuf?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}