package zarak.multibags.backend.ovarlay

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.MouseHelper
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.screen.inventory.CreativeScreen
import net.minecraft.client.gui.screen.inventory.FurnaceScreen
import net.minecraft.client.util.InputMappings
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.items.ItemStackHandler
import org.lwjgl.glfw.GLFW
import zarak.multibags.References
import zarak.multibags.backend.Rect
import zarak.multibags.backend.SyncVariables
import zarak.multibags.networking.PacketSystem
import zarak.multibags.networking.packets.CMessageClickOverlay

object CBagOverlay : AbstractGui(), AbsBagOverlay {
    var lastContainerScreen: ContainerScreen<*> = FurnaceScreen(null, null, null) //dummy start
        set(value) {
            if (field != value) {
                rebuild()
                field = value
            }
        }
    const val slotSize = 20
    val slots = hashMapOf<Int, Pair<Int, Int>>()//id, (x,y)
    var h = 0
    var w = 0
    val mc = Minecraft.getInstance()
    lateinit var bagInv: ItemStackHandler
    fun rebuild() {
        w = 0
        h = 0
        slots.clear()
        bagInv = getData().getBagInv()
        repeat(bagInv.slots) {
            val x = it / 9 * (slotSize)
            val y = it % 9 * (slotSize)
            slots[it] = Pair(x, y)
        }
        w = (bagInv.slots / 9 * slotSize)
        h = (9 * slotSize)

    }

    fun draw(matrixStack: MatrixStack, mx: Int, my: Int) {
        if (canBeRendered()) {
            val start = slotStartPos()
            slots.forEach { (id, point) ->
                val x = point.first + start.first
                val y = point.second + start.second
                drawSlot(matrixStack, id, mx, my, x, y)
            }
        }
        /*  matrixStack.translate(
              55.0, 11.0, 0.0
          )*/
        //   fill(matrixStack, 0, 0, 100, 100, 0xFFFFFFFF.toInt())
    }

    fun mouseRelease(mx: Int, my: Int, btn: Int): Boolean {
        if (canBeRendered()) {
            val start = slotStartPos()
            return slots.filter {
                Rect(it.value.first + start.first, it.value.second + start.second, slotSize).contains(mx, my)
            }.keys.firstOrNull()?.also { clickSlot(it, btn) } != null
        }
        return false
    }

    fun clickSlot(id: Int, btn: Int) {
        fun isShift(): Boolean {
            return InputMappings.isKeyDown(mc.window.window, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                    InputMappings.isKeyDown(mc.window.window, GLFW.GLFW_KEY_RIGHT_SHIFT)
        }

        fun isLeftMouse(): Boolean {
            return btn == 0
        }

        fun isRightMouse(): Boolean {
            return btn == 1
        }
        if (isShift()) {
            PacketSystem.INSTANCE.sendToServer(CMessageClickOverlay(id, ClickType.PICK_ONE))
        } else {
            if (isLeftMouse()) {
                PacketSystem.INSTANCE.sendToServer(CMessageClickOverlay(id, ClickType.PICK_ALL))
            } else {
                PacketSystem.INSTANCE.sendToServer(CMessageClickOverlay(id, ClickType.PICK_MID))
            }
        }
    }

    fun slotStartPos(): Pair<Int, Int>{
        return Pair(lastContainerScreen.guiLeft - w - 5, lastContainerScreen.guiTop +(lastContainerScreen.ySize-h)/2)
    }

    fun canBeRendered(): Boolean {
        return slotStartPos().first > 0 && lastContainerScreen !is CreativeScreen
    }

    fun color(color: Number) {
        val colorI = color.toInt()
        val colorA = (colorI shr 24 and 255).toFloat() / 255.0f
        val colorR = (colorI shr 16 and 255).toFloat() / 255.0f
        val colorG = (colorI shr 8 and 255).toFloat() / 255.0f
        val colorB = (colorI and 255).toFloat() / 255.0f
        color(colorR, colorG, colorB, colorA)
    }

    fun color(r: Number, g: Number, b: Number, a: Number) {
        RenderSystem.color4f(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
    }

    fun drawSlot(matrixStack: MatrixStack, id: Int, mx: Int, my: Int, x: Int, y: Int) {
        mc.textureManager.bind(ResourceLocation(References.ID, "textures/gui/slot_img.png"))
        RenderSystem.enableBlend()
        color(getData().getBagItem()?.color ?: 0xFFFFFFFF)
        blit(matrixStack, x, y, 0F, 0F, 20, 20, 20, 20)
        color(0x77AAAAAA)
        blit(matrixStack, x, y, 0F, 0F, 20, 20, 20, 20)
        color(0xFFFFFFFF)
        val addPadding = (slotSize - 16) / 2
        val colorGrad = if (Rect(x, y, slotSize).contains(mx, my)) 0x99FFFFFF.toInt() else 0x22FFFFFF
        fillGradient(
            matrixStack,
            x + addPadding,
            y + addPadding,
            x + slotSize - addPadding,
            y + slotSize - addPadding,
            colorGrad,
            colorGrad
        )
        renderStack(bagInv.getStackInSlot(id), x + addPadding, y + addPadding, null)
    }

    private fun renderStack(stack: ItemStack, x: Int, y: Int, label: String?) {
        blitOffset = 200
        mc.itemRenderer.blitOffset = 200.0f
        var font = stack.item.getFontRenderer(stack)
        if (font == null)
            font = mc.font
        mc.itemRenderer.renderAndDecorateItem(stack, x, y)
        mc.itemRenderer.renderGuiItemDecorations(font!!, stack, x, y, label)
        blitOffset = 0
        mc.itemRenderer.blitOffset = 0.0f
    }

    override fun getData() = SyncVariables.data

}