package uno.rebellious.advancedfactory.networking

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import uno.rebellious.advancedfactory.tile.TileEntityController


class FactoryContentsMessageHandler : IMessageHandler<FactoryContentsMessage, IMessage> {
    override fun onMessage(message: FactoryContentsMessage, ctx: MessageContext): IMessage? {
        Minecraft.getMinecraft().addScheduledTask({
            val pos = message.controllerPos
            if (pos != null) {
                val tile = Minecraft.getMinecraft().world.getTileEntity(pos)
                if (tile is TileEntityController) {
                    tile.checkNeighbours(true)
                }
            }
        })
        return null
    }
}

class FactoryProgramMessageHandler : IMessageHandler<FactoryProgramMessage, IMessage> {
    override fun onMessage(message: FactoryProgramMessage, ctx: MessageContext): IMessage? {
        val serverWorld = ctx.serverHandler.player.serverWorld
        serverWorld.addScheduledTask({
            val tile = serverWorld.getTileEntity(message.controller)
            if (tile is TileEntityController) {
                tile.updateFactoryProgram(message.factoryProgram)
            }
        })
        return null
    }
}