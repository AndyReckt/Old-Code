/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.whitelist

enum class WhitelistType(val displayName: String, val disallowMessage: String) {

    NONE("None", ""),
    PURCHASED("Purchased", "§6§lVyrix Network §fis currently under whitelist.\n§ePurchase a whitelist or rank on our store.\n§f§ohttps://store.vyrix.us"),
    MAINTENANCE("Maintenance", "§6§lVyrix Network §fis currently under §4§lMAINTENANCE §fmode.");

    fun isAboveOrEqual(type: WhitelistType): Boolean {
        return this.ordinal >= type.ordinal
    }

    fun getPermission(): String {
        return "whitelist.access.${this.name}"
    }
}