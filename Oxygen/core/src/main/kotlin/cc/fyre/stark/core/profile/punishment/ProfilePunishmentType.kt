/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.profile.punishment

enum class ProfilePunishmentType constructor(val action: String, val color: String, vararg kickMessages: String) {

    BLACKLIST("blacklisted", "&4", "&cYou've been blacklisted from the &6&lVyrix Network&c.", "", "&cThis punishment cannot be appealed."),
    BAN("banned", "&c", "&cYou've been banned from the &6&lVyrix Network&c.", "", "&cThis punishment will last %time%.", "&cThink this is false? Join ts.vyrix.us"),
    MUTE("muted", "&e"),
    WARN("warned", "&a");

    val kickMessages: List<String> = listOf(*kickMessages)

}
