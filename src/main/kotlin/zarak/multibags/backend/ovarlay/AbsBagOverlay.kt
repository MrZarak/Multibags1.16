package zarak.multibags.backend.ovarlay

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemStackHandler
import zarak.multibags.backend.DataPlayerMB

interface AbsBagOverlay {
    abstract fun getData(): DataPlayerMB
    fun genBagInv(): ItemStackHandler {
        return getData().getBagInv()
    }
}