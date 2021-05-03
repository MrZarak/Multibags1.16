package zarak.multibags.registry

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import zarak.multibags.References

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object BlockRegistry {
    val BLOCKS = KDeferredRegister(ForgeRegistries.BLOCKS, References.ID)

    private fun createBlockWithItem(name: String, supplier: (String) -> Block): ObjectHolderDelegate<Block> {
        return BLOCKS.registerObject(name) { supplier.invoke(name) }.also {
            ItemRegistry.ITEMS.registerObject(name) { BlockItem(it.get(), Item.Properties().tab(References.TAB)) }
        }
    }
}