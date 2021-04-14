/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.sign

import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 9/3/2020.
 */
interface SignGUIListener {
    fun onSignDone(param1Player: Player, param1ArrayOfString: Array<String?>)
}