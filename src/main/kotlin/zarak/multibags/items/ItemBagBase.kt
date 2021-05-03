package zarak.multibags.items

import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

import zarak.multibags.References
import zarak.multibags.container.ContainerMultibagsInv


open class ItemBagBase(name: String, val level: Int, val durability: Int, val color: Int) : ItemBase(
    name, Properties().tab(References.TAB).defaultDurability(durability)
) {

}