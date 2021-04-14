/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors
import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 8/5/2020.
 */
@Getter
@Setter
@Accessors(fluent = true)
class ProgressBarBuilder(blocksToDisplay: Int) {

    val blocksToDisplay: Int
    var blockChar = 0.toChar()
    var completedColor: String? = null
    var uncompletedColor: String? = null

    constructor() : this(10) {
        blockChar = StringEscapeUtils.unescapeJava("\u258C")[0]
        completedColor = ChatColor.GREEN.toString()
        uncompletedColor = ChatColor.GRAY.toString()
    }

    fun build(percentage: Double): String {
        var percentage = percentage
        val blocks = arrayOfNulls<String>(blocksToDisplay)
        Arrays.fill(blocks, uncompletedColor + blockChar)
        if (percentage > 100.0) {
            percentage = 100.0
        }
        var i = 0
        while (i < percentage / 10) {
            blocks[i] = completedColor + blockChar
            i++
        }
        return StringUtils.join(blocks)
    }

    companion object {
        fun percentage(value: Int, goal: Int): Double {
            return if (value > goal) 100.0 else value.toDouble() / goal.toDouble() * 100.0
        }
    }

    init {
        this.blocksToDisplay = blocksToDisplay
    }
}
