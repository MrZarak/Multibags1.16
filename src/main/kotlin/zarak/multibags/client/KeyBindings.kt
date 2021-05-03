package zarak.multibags.client

import net.minecraft.client.settings.KeyBinding
import net.minecraft.client.util.InputMappings
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.glfw.GLFW
import zarak.multibags.References

object KeyBindings {
    val KEY_OPEN_GUI = KeyBinding(
        "key_open_main_gui",
        KeyConflictContext.IN_GAME,
        InputMappings.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        References.ID.capitalize()
    )

    fun init() {
        ClientRegistry.registerKeyBinding(KEY_OPEN_GUI)
    }
}