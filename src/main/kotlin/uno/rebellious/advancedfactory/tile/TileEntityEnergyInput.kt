package uno.rebellious.advancedfactory.tile

import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.EnergyStorage
import uno.rebellious.advancedfactory.handler.EnergyDirection
import uno.rebellious.advancedfactory.handler.EnergyHandler
import uno.rebellious.advancedfactory.util.Types

class TileEntityEnergyInput: TileEntityAdvancedFactory(), ICapabilityProvider, IAdvancedFactoryEnergyTile {
    override val maxEnergy = 50000
    override val storage = EnergyStorage(maxEnergy)

    override var itemInventory: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    override val factoryBlockType = Types.ENERGY_INPUT
    override var controllerTile: BlockPos? = null

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        this.controllerTile ?: return super.hasCapability(capability, facing)
        if (capability == CapabilityEnergy.ENERGY) {
            return true
        }
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        this.controllerTile ?: return super.getCapability(capability, facing)
        if (capability == CapabilityEnergy.ENERGY) return EnergyHandler(
            this,
            EnergyDirection.INPUT
        ) as T
        return super.getCapability(capability, facing)
    }
}