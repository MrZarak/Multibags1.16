package zarak.multibags.backend

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import zarak.multibags.References
import zarak.multibags.backend.ovarlay.CBagOverlay
import zarak.multibags.client.KeyBindings
import zarak.multibags.networking.PacketSystem
import zarak.multibags.networking.packets.CMessageOpenGui

@Mod.EventBusSubscriber(Dist.CLIENT, modid = References.ID)
object ClientEvents {
    @SubscribeEvent
    fun onGuiRender(event: GuiScreenEvent) {
        val gui = event.gui
        if (gui is ContainerScreen<*>) {
            val window = gui.minecraft.window
            val mouseHandler = gui.minecraft.mouseHandler
            val mx = (mouseHandler.xpos() * window.guiScaledWidth / window.screenWidth).toInt()
            val my = (mouseHandler.ypos() * window.guiScaledHeight / window.screenHeight).toInt()
            CBagOverlay.lastContainerScreen = gui
            if (CBagOverlay.canBeRendered()) {
                when (event) {
                    is GuiScreenEvent.DrawScreenEvent.Post -> {
                        CBagOverlay.draw(event.matrixStack, mx, my)
                    }
                    is GuiScreenEvent.MouseReleasedEvent.Pre -> {
                        if (CBagOverlay.mouseRelease(mx, my, event.button)) {
                            event.isCanceled = true
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onClick(event: InputEvent.KeyInputEvent) {
        if (event.key == KeyBindings.KEY_OPEN_GUI.key.value && Minecraft.getInstance().screen == null) {
            PacketSystem.INSTANCE.sendToServer(CMessageOpenGui())
        }
    }
}