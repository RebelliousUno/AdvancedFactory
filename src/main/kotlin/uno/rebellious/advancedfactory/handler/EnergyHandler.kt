package uno.rebellious.advancedfactory.handler

import net.minecraftforge.energy.EnergyStorage
import net.minecraftforge.energy.IEnergyStorage
import uno.rebellious.advancedfactory.tile.IAdvancedFactoryEnergyTile
import uno.rebellious.advancedfactory.tile.TileEntityEnergyInput

public class EnergyHandler(var tile: IAdvancedFactoryEnergyTile, private val direction: EnergyDirection) : IEnergyStorage {


    override fun canExtract(): Boolean {
        return direction == EnergyDirection.OUTPUT
    }

    override fun getMaxEnergyStored(): Int {
        return tile.storage.maxEnergyStored
    }

    override fun getEnergyStored(): Int {
        return tile.storage.energyStored
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        return tile.storage.extractEnergy(maxExtract, simulate)
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        return tile.storage.receiveEnergy(maxReceive, simulate)
    }

    override fun canReceive(): Boolean {
        return direction == EnergyDirection.INPUT
    }

}