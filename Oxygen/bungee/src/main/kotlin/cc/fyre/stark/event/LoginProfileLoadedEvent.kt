/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.event

import cc.fyre.stark.profile.ProxyProfile
import net.md_5.bungee.api.plugin.Cancellable
import net.md_5.bungee.api.plugin.Event

class LoginProfileLoadedEvent(val profile: ProxyProfile) : Event(), Cancellable {

    private var cancelled: Boolean = false
    var cancelReasons: String? = null

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }
}
