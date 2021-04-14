/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.nametag

import org.bukkit.entity.Player

internal class NametagUpdate(var toRefresh: String, var refreshFor: String?) {
    constructor(toRefresh: Player) : this(toRefresh.name, null)
    constructor(toRefresh: Player, refreshFor: Player) : this(toRefresh.name, refreshFor.name)
}
