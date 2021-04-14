/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.rank.command

import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.rank.Rank
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import com.mongodb.client.model.Filters
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/21/2020.
 */
object RankDeleteCommand {

    @Command(["rank delete"], permission = "op")
    @JvmStatic
    fun rankDelete(sender: Player, @Param("rank") rank: Rank) {
        if (StarkCore.instance.rankHandler.getByName(rank.displayName) == null) {
            sender.sendMessage("${ChatColor.RED}A rank with this rank does not exist.")
            return
        }
        StarkCore.instance.rankHandler.ranks.remove(rank.id)

        StarkCore.instance.mongo.database.getCollection("ranks")
                .deleteOne(Filters.eq("id", rank.id))

        Bukkit.getOnlinePlayers().forEach {
            StarkCore.instance.getProfileHandler().pullProfileUpdates(it.uniqueId)
        }
        sender.sendMessage("${ChatColor.GREEN}Successfully removed the rank ${rank.displayName}")
    }
}