/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile.grant.command

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 8/9/2020.
 */
object ClearGrantsCommand {

    @Command(["cleargrants"], "op", description = "clear grants", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        target.grants.clear()
        Stark.instance.core.getProfileHandler().saveProfile(target)
    }
}