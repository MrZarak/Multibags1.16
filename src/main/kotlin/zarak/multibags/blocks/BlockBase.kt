package zarak.multibags.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material

open class BlockBase(val name:String, properties: Properties): Block(properties) {
    constructor(name:String):this(name, Properties.of(Material.STONE).strength(1.2F))
}