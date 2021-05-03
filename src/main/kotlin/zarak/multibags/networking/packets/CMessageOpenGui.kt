package zarak.multibags.networking.packets

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import zarak.multibags.container.ContainerMultibagsInv
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Supplier

class CMessageOpenGui {
    companion object {
        val toBytes = BiConsumer<CMessageOpenGui, PacketBuffer> { msg, buffer ->
        }
        val fromBytes = Function<PacketBuffer, CMessageOpenGui> { buffer ->
            CMessageOpenGui()
        }
        val handler = BiConsumer<CMessageOpenGui, Supplier<NetworkEvent.Context>> { msg, ctxGet ->
            val get = ctxGet.get()
            if (get.direction == NetworkDirection.PLAY_TO_SERVER) {
                get.sender?.openMenu(ContainerMultibagsInv.PROVIDER)
            }
            get.packetHandled = true
        }
    }
}