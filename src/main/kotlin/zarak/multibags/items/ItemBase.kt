package zarak.multibags.items

import net.minecraft.item.Item
import zarak.multibags.References


open class ItemBase(val name:String, properties: Properties): Item(properties) {
    constructor(name:String):this(name, Properties().tab(References.TAB))
}