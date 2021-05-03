package zarak.multibags.backend

import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import zarak.multibags.References

@Mod.EventBusSubscriber(modid = References.ID)
object ServerEvents {
    @SubscribeEvent
    fun tick(event:TickEvent.ServerTickEvent){
        if(event.phase == TickEvent.Phase.START){
            DataPlayerMB.update(false)
        }
    }
}