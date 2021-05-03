package zarak.multibags.registry

import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.network.PacketBuffer
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.runWhenOn
import zarak.multibags.References
import zarak.multibags.container.ContainerMultibagsInv

object ContainerRegistry {
    val CONTAINERS = KDeferredRegister(ForgeRegistries.CONTAINERS, References.ID)
    val MULTIBAGS by registerContainer(
        "multibags",
        { i, inv, _ -> ContainerMultibagsInv(i, inv.player) },
        { cont, inv, component -> zarak.multibags.client.gui.GuiMultibagsInv(cont, inv, component) },
    )


    private fun <T : Container> registerContainer(
        name: String,
        functionContainer: (Int, PlayerInventory, PacketBuffer?) -> T,
        functionScreen: (T, PlayerInventory, ITextComponent?) -> Any
    ): ObjectHolderDelegate<ContainerType<T>> {
        return CONTAINERS.registerObject(name) {
            IForgeContainerType.create { windowId, inv, data ->
                functionContainer.invoke(windowId, inv, data)
            }.also {
                runWhenOn(Dist.CLIENT) {
                    ScreenManager.register(it) { cont, inv, component ->
                        functionScreen.invoke(cont, inv, component) as ContainerScreen<T>
                    }
                }
            }
        }
    }
}