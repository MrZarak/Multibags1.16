package zarak.multibags.networking.packets

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import zarak.multibags.backend.DataPlayerMB
import zarak.multibags.backend.SyncVariables
import zarak.multibags.backend.ovarlay.CBagOverlay
import zarak.multibags.backend.ovarlay.ClickType
import zarak.multibags.backend.ovarlay.SBagOverlay
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

data class CMessageClickOverlay(val id: Int, val type: ClickType) {
    companion object {
        val toBytes = BiConsumer<CMessageClickOverlay, PacketBuffer> { msg, buffer ->
            buffer.writeInt(msg.id)
            buffer.writeInt(msg.type.ordinal)
        }
        val fromBytes = Function<PacketBuffer, CMessageClickOverlay> { buffer ->
            CMessageClickOverlay(buffer.readInt(), ClickType.values()[buffer.readInt()])
        }
        val handler = BiConsumer<CMessageClickOverlay, Supplier<NetworkEvent.Context>> { msg, ctxGet ->
            val get = ctxGet.get()
            if (get.direction == NetworkDirection.PLAY_TO_SERVER) {
                get.enqueueWork {
                    SBagOverlay.setPlayer(get.sender!!)
                    SBagOverlay.click(msg.id, msg.type)
                }
            }
            get.packetHandled = true
        }
    }
}