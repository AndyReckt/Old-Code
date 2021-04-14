/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.tags.commands

import cc.fyre.stark.Stark
import cc.fyre.stark.core.StarkCore
import cc.fyre.stark.core.tags.Tag
import cc.fyre.stark.core.tags.TagType
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.tags.menu.TagSelectionMenu
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Missionary (missionarymc@gmail.com)
 * @since 4/26/2020
 */
object TagsCommands {

    @JvmStatic
    @Command(names = ["tag list"], description = "List all tags on the server", permission = "op")
    fun listTags(sender: CommandSender) {
        sender.sendMessage("${ChatColor.YELLOW}Listing all tags...")
        for (tag in StarkCore.instance.tagHandler.getTags().values) { // - name (display - before/after)
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "${ChatColor.GRAY} - ${ChatColor.YELLOW}${tag.name} ${ChatColor.GRAY}(${tag.display}${ChatColor.GRAY} - ${getColorBasedOnTagType(tag.tagType)}${tag.tagType.name.capitalize()})"))
        }
    }

    @JvmStatic
    @Command(names = ["tag create"], description = "Create a tag", permission = "op")
    fun createTag(sender: CommandSender, @Param(name = "name") name: String, @Param(name = "display") display: String, @Param(name = "tagType", defaultValue = "BEFORE") tagType: TagType) {
        val tagHandler = StarkCore.instance.tagHandler

        if (tagHandler.getTags().containsKey(name)) {
            sender.sendMessage("${ChatColor.RED}A tag by the name of $name already exists! \nRun /tag list to see all tags!")
            return
        }

        val newTag = Tag(name, display, tagType)
        tagHandler.getTags()[newTag.name] = newTag
        sender.sendMessage("${ChatColor.GREEN}Created the ${getColorBasedOnTagType(tagType)}$name${ChatColor.GREEN} tag!")
        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            tagHandler.save(newTag)
        }
    }

    @JvmStatic
    @Command(names = ["tag delete", "tag remove"], description = "Delete a tag", permission = "op")
    fun deleteTag(sender: CommandSender, @Param(name = "tag") tag: Tag) {
        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            StarkCore.instance.tagHandler.delete(tag)
            sender.sendMessage("${ChatColor.GREEN}Deleted ${tag.name} tag.")
        }
    }

    @JvmStatic
    @Command(names = ["tag", "tags", "prefix", "suffix", "playertags"], description = "Give yourself a tag")
    fun tagCommand(player: Player) {
        TagSelectionMenu().openMenu(player)
    }

    private fun getColorBasedOnTagType(tagType: TagType): ChatColor {
        return when (tagType) {
            TagType.BEFORE -> ChatColor.GREEN
            TagType.AFTER -> ChatColor.BLUE
            else -> ChatColor.WHITE
        }
    }
}