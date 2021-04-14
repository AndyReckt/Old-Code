/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

object SimpleReflection {

    @JvmStatic
    fun setField(obj: Any, field: String, value: Any) {
        val field = obj::class.java.getDeclaredField(field)
        field.isAccessible = true
        field.set(obj, value)
    }

    @JvmStatic
    fun getField(obj: Any, field: String): Any {
        val field = obj::class.java.getDeclaredField(field)
        field.isAccessible = true
        return field.get(obj)
    }

}