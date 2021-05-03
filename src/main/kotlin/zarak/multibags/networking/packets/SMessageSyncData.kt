package zarak.multibags.networking.packets

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import zarak.multibags.backend.DataPlayerMB
import zarak.multibags.backend.SyncVariables
import zarak.multibags.backend.ovarlay.CBagOverlay
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

data class SMessageSyncData(val data: CompoundNBT) {
    companion object {
        val toBytes = BiConsumer<SMessageSyncData, PacketBuffer> { msg, buffer ->
            buffer.writeNbt(msg.data)
        }
        val fromBytes = Function<PacketBuffer, SMessageSyncData> { buffer ->
            SMessageSyncData(buffer.readNbt()!!)
        }
        val handler = BiConsumer<SMessageSyncData, Supplier<NetworkEvent.Context>> { msg, ctxGet ->
            if (ctxGet.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
                SyncVariables.data.fromNbt(msg.data)
                CBagOverlay.rebuild()
            }
            ctxGet.get().packetHandled = true
        }
    }
}