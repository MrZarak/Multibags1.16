package zarak.multibags.container

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.items.SlotItemHandler
import zarak.multibags.backend.DataPlayerMB
import zarak.multibags.backend.SyncVariables
import zarak.multibags.items.ItemBagBase
import zarak.multibags.registry.ContainerRegistry
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class ContainerMultibagsInv(id: Int, val player: PlayerEntity) : Container(ContainerRegistry.MULTIBAGS, id) {
    companion object {
        val PROVIDER = object : INamedContainerProvider {
            override fun createMenu(id: Int, inv: PlayerInventory, player: PlayerEntity): Container {
                return ContainerMultibagsInv(id, player)
            }

            override fun getDisplayName() =
                TranslationTextComponent("container.${ContainerRegistry.MULTIBAGS.registryName!!.path}")
        }
    }

    var data = if (player is ServerPlayerEntity) {
        DataPlayerMB.getOrLoad(player).also {
            it.sync(player)
        }
    } else {
        SyncVariables.data
    }

    init {
        val mainX = 108
        val mainY = 34
        addSlot(object : InnerSlot(0, mainX, mainY) {
            override fun mayPlace(stack: ItemStack): Boolean {
                return stack.item is ItemBagBase
            }
        })
        /*todo val maxBagLevel = 4
        repeat(maxBagLevel + 1) {
            val rotation = Math.PI * 2 / (maxBagLevel + 1) * (it + 1)
            val x = (mainX + sin(rotation) * 32).roundToInt()
            val y = (mainY - 2 + cos(rotation) * 30).roundToInt()
            addSlot(InnerSlot(it + 1, x, y))
        }*/
        //add player slots
        kotlin.run {
            for (l in 0..2) {
                for (j1 in 0..8) {
                    addSlot(Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18))
                }
            }

            for (i1 in 0..8) {
                addSlot(Slot(player.inventory, i1, 8 + i1 * 18, 142))
            }
        }
    }

    override fun stillValid(player: PlayerEntity): Boolean {
        return true
    }

    open inner class InnerSlot(val id: Int, x: Int, y: Int) : SlotItemHandler(data.inv, id, x, y) {
        @OnlyIn(Dist.CLIENT)
        override fun isActive(): Boolean {
            return id == 0 || data.getBagLevel() + 1 >= id
        }

        override fun setChanged() {
            super.setChanged()
            if (player is ServerPlayerEntity) {
                DataPlayerMB.save(player, data)
                data.sync(player)
            }
        }
    }
}