package zarak.multibags

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import zarak.multibags.backend.DataPlayerMB

@Mod.EventBusSubscriber(modid = References.ID)
object EventsCommon {
    @SubscribeEvent
    fun onLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.player as ServerPlayerEntity
        DataPlayerMB.getOrLoad(player).sync(player)
    }
}