package zarak.multibags.registry

import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import zarak.multibags.References
import zarak.multibags.items.ItemBagBase

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object ItemRegistry {
    val ITEMS = KDeferredRegister(ForgeRegistries.ITEMS, References.ID)
    val BAG_1 by createBlockWithItem("bag_first") { ItemBagBase(it, 0, 50, 0xFFFFFFFF.toInt()) }
    val BAG_2 by createBlockWithItem("bag_second") { ItemBagBase(it, 1, 200, 0xFFAA0000.toInt()) }
    val BAG_3 by createBlockWithItem("bag_third") { ItemBagBase(it, 2, 400, 0xFFFFFF00.toInt()) }
    val BAG_4 by createBlockWithItem("bag_fourth") { ItemBagBase(it, 3, 800, 0xFF00FF00.toInt()) }
    val BAG_5 by createBlockWithItem("bag_fifth") { ItemBagBase(it, 4, 1200, 0xFF00FFFF.toInt()) }

    private fun createBlockWithItem(name: String, supplier: (String) -> Item): ObjectHolderDelegate<Item> {
        return ITEMS.registerObject(name) { supplier.invoke(name) }
    }
}