/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.visibility

import org.bukkit.entity.Player

interface OverrideHandler {
    fun getAction(toRefresh: Player, refreshFor: Player): OverrideAction
}