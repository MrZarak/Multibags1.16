package zarak.multibags.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.gui.screen.inventory.InventoryScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import zarak.multibags.References
import zarak.multibags.backend.ovarlay.CBagOverlay
import zarak.multibags.container.ContainerMultibagsInv

class GuiMultibagsInv(container: ContainerMultibagsInv, inv: PlayerInventory, text: ITextComponent?) :
    ContainerScreen<ContainerMultibagsInv>(container, inv, text) {

    override fun render(matrixStack: MatrixStack?, p_230430_2_: Int, p_230430_3_: Int, p_230430_4_: Float) {
        this.renderBackground(matrixStack)
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_)
        this.renderTooltip(matrixStack, p_230430_2_, p_230430_3_)
    }
    override fun renderBg(stack: MatrixStack, pt: Float, mx: Int, my: Int) {
        minecraft!!.getTextureManager().bind(ResourceLocation(References.ID,"textures/gui/multibags_inv.png"))
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight)

        InventoryScreen.renderEntityInInventory(
            leftPos + 34,
            topPos + 73,
            30,
            ((leftPos + 88 - mx).toFloat()),
            ((topPos + 45 - 30 - my).toFloat()),
            minecraft!!.player!!
        )
        getMenu().slots.filter { it.index != 0  && it is ContainerMultibagsInv.InnerSlot }.forEach {
            if(it.isActive) {
                CBagOverlay.color(0xFFFFFFFF)
                stack.pushPose()
                stack.translate(it.x.toDouble() + guiLeft, it.y.toDouble() + guiTop, 0.0)
                minecraft!!.getTextureManager().bind(ResourceLocation(References.ID, "textures/gui/multibags_inv.png"))
                blit(stack, -4, -4, 1, 167, 24, 24)
                stack.popPose()
            }
        }
    }
    override fun renderLabels(p_230451_1_: MatrixStack, p_230451_2_: Int, p_230451_3_: Int) {

    }
}