/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.pagination

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.*

abstract class PaginatedResult<T> @JvmOverloads constructor(private val resultsPerPage: Int = 9) {

    init {
        assert(resultsPerPage > 0)
    }

    fun display(sender: CommandSender, results: Collection<T>, page: Int) {
        this.display(sender, ArrayList(results), page)
    }

    fun display(sender: CommandSender, results: List<T>, page: Int) {
        if (results.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "No entries were found.")
            return
        }

        val maxPages = results.size / this.resultsPerPage + 1

        if (page <= 0 || page > maxPages) {
            sender.sendMessage(ChatColor.RED.toString() + "Page '" + page + "' not found. (" + ChatColor.YELLOW + "1 - " + maxPages + ChatColor.RED + ")")
            return
        }

        sender.sendMessage(this.getHeader(page, maxPages))

        var i = this.resultsPerPage * (page - 1)
        while (i < this.resultsPerPage * page && i < results.size) {
            sender.sendMessage(this.format(results[i], i))
            ++i
        }
    }

    abstract fun getHeader(page: Int, maxPages: Int): String

    abstract fun format(result: T, resultIndex: Int): String

}