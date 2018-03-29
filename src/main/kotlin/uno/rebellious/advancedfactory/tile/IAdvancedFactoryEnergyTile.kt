package uno.rebellious.advancedfactory.tile

import net.minecraftforge.energy.EnergyStorage

interface IAdvancedFactoryEnergyTile {
    val maxEnergy: Int
    val storage: EnergyStorage
}
