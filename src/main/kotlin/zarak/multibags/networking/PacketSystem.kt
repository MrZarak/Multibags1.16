package zarak.multibags.networking

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry
import zarak.multibags.References
import zarak.multibags.networking.packets.CMessageClickOverlay
import zarak.multibags.networking.packets.CMessageOpenGui
import zarak.multibags.networking.packets.SMessageSyncData

@SuppressWarnings("INACCESSIBLE_TYPE")
object PacketSystem {
    private const val PROTOCOL_VERSION = "1"
    val INSTANCE = NetworkRegistry.newSimpleChannel(
        ResourceLocation(References.ID, "main"),
        { PROTOCOL_VERSION },
        { true },
        { true }
    )
    fun init(){
        var id = 0
        INSTANCE.registerMessage(id++,SMessageSyncData::class.java,
            SMessageSyncData.toBytes, SMessageSyncData.fromBytes, SMessageSyncData.handler)
        INSTANCE.registerMessage(id++,CMessageClickOverlay::class.java,
            CMessageClickOverlay.toBytes, CMessageClickOverlay.fromBytes, CMessageClickOverlay.handler)
        INSTANCE.registerMessage(id++,CMessageOpenGui::class.java,
            CMessageOpenGui.toBytes, CMessageOpenGui.fromBytes, CMessageOpenGui.handler)
    }
}