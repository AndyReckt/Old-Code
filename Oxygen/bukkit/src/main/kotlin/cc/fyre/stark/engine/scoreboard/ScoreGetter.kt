/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.scoreboard

import org.bukkit.entity.Player
import java.util.*

interface ScoreGetter {

    fun getScores(scores: LinkedList<String>, player: Player)

}