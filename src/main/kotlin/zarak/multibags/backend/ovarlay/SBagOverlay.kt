package zarak.multibags.backend.ovarlay

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraftforge.items.ItemStackHandler
import zarak.multibags.backend.DataPlayerMB
import zarak.multibags.items.ItemBagBase
import java.util.*
import kotlin.math.max
import kotlin.math.min

object SBagOverlay {
    private var currentPlayer: ServerPlayerEntity? = null
    lateinit var bagInv: ItemStackHandler

    fun setPlayer(player: ServerPlayerEntity) {
        currentPlayer = player
        rebuild()
    }

    fun rebuild() {
        bagInv = getData().getBagInv()
    }

    fun click(id: Int, type: ClickType) {
        val pInv = currentPlayer!!.inventory
        val playerStack = pInv.carried
        val slotStack = bagInv.getStackInSlot(id)
        var changed = false
        fun setStack(stack: ItemStack) {
            bagInv.setStackInSlot(id, stack)
            changed = true
        }
        if (!playerStack.isEmpty && playerStack.item !is ItemBagBase) {
            if (slotStack.isEmpty) {
                pInv.carried = ItemStack.EMPTY
                setStack(playerStack)
            } else if (
                slotStack.item == playerStack.item &&
                slotStack.damageValue == playerStack.damageValue &&
                Objects.equals(playerStack.tag, slotStack.tag)
            ) {
                val toAdd = min(playerStack.count, slotStack.maxStackSize - slotStack.count)
                playerStack.shrink(toAdd)
                slotStack.grow(toAdd)
                changed = true
            }
        } else if (!slotStack.isEmpty) {
            when (type) {
                ClickType.PICK_ALL -> {
                    setStack(ItemStack.EMPTY)
                    pInv.carried = slotStack
                }
                ClickType.PICK_MID -> {
                    val count = max(1, slotStack.count / 2)
                    val copy = slotStack.copy()
                    copy.count = count
                    slotStack.shrink(count)
                    pInv.carried = copy
                    changed = true
                }
                ClickType.PICK_ONE -> {
                    val count = 1
                    val copy = slotStack.copy()
                    copy.count = count
                    slotStack.shrink(count)
                    pInv.carried = copy
                    changed = true
                }
            }
        }
        if (changed) {
            currentPlayer!!.broadcastCarriedItem()
            val tag = bagInv.serializeNBT()
             getData().let { data->
                 data.getBagTag().put("bag_inv", tag)
                 data.sync(currentPlayer!!)
                 DataPlayerMB.save(currentPlayer!!, data)
            }
        }
    }

    fun getData() = DataPlayerMB.getOrLoad(currentPlayer!!)
}