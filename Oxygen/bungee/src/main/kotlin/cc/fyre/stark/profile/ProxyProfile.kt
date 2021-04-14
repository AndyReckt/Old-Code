/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.core.profile.Profile
import java.util.*

class ProxyProfile(uuid: UUID) : Profile(uuid) {

    override fun apply() {
        val player = Stark.instance.proxy.getPlayer(uuid)

        if (player != null) {
            getCompoundedPermissions().forEach {
                if (it.startsWith("-")) {
                    player.setPermission(it.substring(1), false)
                } else {
                    player.setPermission(it, true)
                }
            }
        }
    }
}