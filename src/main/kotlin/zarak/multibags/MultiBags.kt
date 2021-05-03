package zarak.multibags

import net.minecraft.client.gui.ScreenManager
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent
import thedarkcolour.kotlinforforge.forge.*
import zarak.multibags.backend.DataPlayerMB
import zarak.multibags.client.KeyBindings
import zarak.multibags.networking.PacketSystem
import zarak.multibags.registry.BlockRegistry
import zarak.multibags.registry.ContainerRegistry
import zarak.multibags.registry.ItemRegistry

@Mod(References.ID)
object MultiBags {
    init {
        PacketSystem.init()

        MOD_BUS.addListener<FMLCommonSetupEvent> {
        }
        MOD_BUS.addListener<FMLServerStartingEvent> {

        }
        MOD_BUS.addListener<FMLServerStoppingEvent> {
            DataPlayerMB.update(true)
        }
        callWhenOn(Dist.CLIENT) {
            MOD_BUS.addListener<FMLClientSetupEvent> {
                KeyBindings.init()
            }
        }
        BlockRegistry.BLOCKS.register(MOD_BUS)
        ItemRegistry.ITEMS.register(MOD_BUS)
        ContainerRegistry.CONTAINERS.register(MOD_BUS)
    }
}