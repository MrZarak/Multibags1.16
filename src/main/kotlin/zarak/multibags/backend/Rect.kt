package zarak.multibags.backend

import java.lang.IllegalArgumentException
import java.util.*

data class Rect(var x: Double, var y: Double, var w: Double, var h: Double) {
    constructor(x: Number, y: Number, w: Number, h: Number) : this(
        x.toDouble(),
        y.toDouble(),
        w.toDouble(),
        h.toDouble()
    )
    constructor(x: Number, y: Number, s: Number) : this(
        x.toDouble(),
        y.toDouble(),
        s.toDouble(),
        s.toDouble()
    )
    fun contains(mx: Number, my: Number): Boolean {
        val dMx = mx.toDouble()
        val dMy = my.toDouble()
        return dMx in x..(x + w) && dMy in y..(y + h)
    }

    fun subRect(xS: Number, yS: Number, wS: Number, hS: Number): Rect {
        val xSD = xS.toDouble()
        val ySD = yS.toDouble()
        val wSD = wS.toDouble()
        val hSD = hS.toDouble()
        if (xSD < 0 || ySD < 0 || xSD + wSD > w || ySD + hSD > h) {
            throw IllegalArgumentException("Sub rect out of range: x=$x, y=$y,w=$w,h=$h")
        }
        return Rect(x + xSD, y + ySD, wSD, hSD)
    }
    fun rescaledRect(wS: Number, hS: Number): Rect {
        val wSD = wS.toDouble()
        val hSD = hS.toDouble()
        return Rect(x , y , w+wSD, h+hSD)
    }

    override fun equals(other: Any?): Boolean {
        return other is Rect && other.x == this.x && other.y == this.y && other.w == this.w && other.h == this.h
    }
}
