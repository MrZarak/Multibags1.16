package zarak.multibags.backend

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.fml.network.PacketDistributor
import net.minecraftforge.items.ItemStackHandler
import zarak.multibags.References
import zarak.multibags.items.ItemBagBase
import zarak.multibags.networking.PacketSystem
import zarak.multibags.networking.packets.SMessageSyncData
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DataPlayerMB {
    companion object {
        private val cache = hashMapOf<String, DataPlayerMB>()
        private val toSave = hashSetOf<Pair<String, DataPlayerMB>>()
        private var ticker = 0
        fun update(immediately: Boolean) {
            if (toSave.isNotEmpty()) {
                if (ticker++ == 100 || immediately) {
                    toSave.forEach { save0(it.first, it.second) }
                    toSave.clear()
                    ticker = 0
                }
            }
        }

        fun getOrLoad(player: ServerPlayerEntity): DataPlayerMB {
            return getOrLoad(player.gameProfile.name)
        }

        fun getOrLoad(name: String): DataPlayerMB {
            val path = getPath(name)
            val cached = cache[name]
            if (cached != null) {
                return cached
            }
            if (!Files.exists(path)) {
                return DataPlayerMB()
            }
            return Files.newBufferedReader(path).use {
                References.GSON.fromJson(it, DataPlayerMB::class.java)
            }.also { cache[name] = it }
        }

        fun save(player: ServerPlayerEntity, data: DataPlayerMB = getOrLoad(player.gameProfile.name)) {
            save0(player.gameProfile.name,data)
//            toSave.add(Pair(player.gameProfile.name, data))
        }

        private fun save0(name: String, data: DataPlayerMB = getOrLoad(name)) {
            getPath(name).let {
                Files.createDirectories(it.parent)
                Files.write(it, References.GSON.toJson(data).toByteArray())
            }
        }

        fun getPath(name: String): Path {
            return Paths.get("${References.ID}/data/$name.json")
        }
    }

    val inv: ItemStackHandler = ItemStackHandler(6)

    fun toNbt(nbt: CompoundNBT): CompoundNBT {
        nbt.put("inv", inv.serializeNBT())
        return nbt
    }

    fun fromNbt(nbt: CompoundNBT) {
        inv.deserializeNBT(nbt.getCompound("inv"))
    }

    fun getBag(): ItemStack = inv.getStackInSlot(0)

    fun getBagItem(): ItemBagBase? {
        return getBag().item.takeIf { it is ItemBagBase } as? ItemBagBase
    }

    fun getBagLevel(): Int {
        return getBagItem()?.level ?: -1
    }

    fun getBagTag(): CompoundNBT {
        return getBag().orCreateTag
    }

    fun getBagInv(): ItemStackHandler {
        val size = (getBagLevel() + 1) * 9
        val itemStackHandler = ItemStackHandler(size)
        itemStackHandler.deserializeNBT(getBagTag().getCompound("bag_inv"))
        return itemStackHandler
    }

    fun sync(player: ServerPlayerEntity) {
        PacketSystem.INSTANCE.send(
            PacketDistributor.PLAYER.with { player },
            SMessageSyncData(toNbt(CompoundNBT()))
        )
    }
}