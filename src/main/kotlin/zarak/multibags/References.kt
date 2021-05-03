package zarak.multibags

import com.google.gson.*
import net.minecraft.block.Blocks
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.JsonToNBT
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import zarak.multibags.backend.DataPlayerMB

object References {
    const val ID = "multibags"
    val TAB = object : ItemGroup("") {
        @OnlyIn(Dist.CLIENT)
        override fun makeIcon(): ItemStack {
            return ItemStack(Blocks.BOOKSHELF)
        }
    }
    val GSON = GsonBuilder()
        .registerTypeAdapter(DataPlayerMB::class.java, JsonSerializer { data: DataPlayerMB, typeOfSrc, context ->
            val compound = CompoundNBT().also {
                data.toNbt(it)
            }
            JsonPrimitive(compound.toString())
        })
        .registerTypeAdapter(DataPlayerMB::class.java, JsonDeserializer { json, typeOfSrc, context ->
            DataPlayerMB().apply {
                fromNbt(json.asString.let {
                    JsonToNBT.parseTag(it)
                })
            }
        })
        .disableHtmlEscaping()
        .create()
}