/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.tab

import org.bukkit.entity.Player

interface LayoutProvider {

    fun provide(player: Player): TabLayout

}