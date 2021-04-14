/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import net.minecraft.server.v1_7_R4.EntityInsentient
import net.minecraft.server.v1_7_R4.EntityTypes

object NMSEntityUtil {

    /**
     * See <a href="https://bukkit.org/threads/nms-custom-entity-is-like-invisible.322708/#post-2897391">[NMS] Custom Entity is like invisible</a>
     */
    @JvmStatic
    fun registerEntity(name: String?, id: Int, customClass: Class<out EntityInsentient?>?) {
        try {
            val method = EntityTypes::class.java.getDeclaredMethod("a", Class::class.java, String::class.java, Int::class.javaPrimitiveType)
            method.isAccessible = true
            method.invoke(null, customClass, name, id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}