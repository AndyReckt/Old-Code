/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.defaults.commands.other

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.profile.BukkitProfile
import cc.fyre.stark.util.CC
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.util.com.mojang.authlib.GameProfile
import net.minecraft.util.com.mojang.authlib.properties.Property
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Player
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL


/**
 * Created by DaddyDombo daddydombo@gmail.com on 1/24/2020.
 */
object DisguiseCommand {

    @Command(names = ["disguise", "nick"], permission = "stark.command.disguise", description = "disguse", async = true)
    @JvmStatic
    fun command(sender: CommandSender, @Param("username") Profile: BukkitProfile) {
        if (sender is Player) {
            if (sender.isDisguised) {
                sender.undisguise()
            }

            Bukkit.getOnlinePlayers().stream().map { p ->
                p.hidePlayer(sender.player)
            }

            sender.disguise(Stark.instance.core.uuidCache.name(Profile.uuid))

            if (!setSkin((sender as CraftPlayer).handle.disguiseProfile, Stark.instance.core.uuidCache.name(Profile.uuid))) {
                sender.sendMessage(CC.RED + "Couldn't retrieve skin!")
            }

            Stark.instance.nametagEngine.reloadPlayer(sender as Player)
            Stark.instance.nametagEngine.reloadOthersFor(sender as Player)

            Bukkit.getOnlinePlayers().stream().map { p ->
                p.showPlayer(sender.player)
            }

            sender.sendMessage("${CC.GREEN}You are now disguised as ${Profile.getDisplayName()}.")
        }
    }

    @Command(names = ["undisguise", "unnick"], permission = "stark.command.disguise", description = "disguse", async = true)
    @JvmStatic
    fun undisguse(sender: CommandSender) {
        if (sender is Player) {
            if (sender.isDisguised) {
                sender.undisguise()
                sender.sendMessage("${CC.GREEN}You are no longer disguised.")
            } else {
                sender.sendMessage("${CC.RED}You aren't disguised!")
            }
        }
    }

//    private fun setSkin(profile: GameProfile, uuid: UUID?): Boolean {
//        return try {
//            val connection: HttpsURLConnection = URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection() as HttpsURLConnection
//            if (connection.responseCode === HttpsURLConnection.HTTP_OK) {
//                val reply = BufferedReader(InputStreamReader(connection.inputStream)).readLine()
////                val skin = reply.split("\"value\":\"".toRegex()).toTypedArray()[1].split("\"".toRegex()).toTypedArray()[0]
////                val signature = reply.split("\"signature\":\"".toRegex()).toTypedArray()[1].split("\"".toRegex()).toTypedArray()[0]
////                profile.properties.put("textures", Property("textures", skin, signature))
//                Bukkit.getServer().broadcastMessage(reply)
//                true
//            } else {
//                println("Connection could not be opened (Response code " + connection.responseCode.toString() + ", " + connection.responseMessage.toString() + ")")
//                false
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            false
//        }
//    }

    private fun setSkin(profile: GameProfile, name: String): Boolean {
        var returnBoolean = false
        val data = getFromName(name)
        val value = data?.get(0)
        val signature = data?.get(1)
        profile.properties.put("textures", Property("textures", value, signature))
        returnBoolean = true
        return returnBoolean
    }

    fun getFromName(name: String): Array<String>? {
        return try {
            val url_0 = URL("https://api.mojang.com/users/profiles/minecraft/$name")
            val reader_0 = InputStreamReader(url_0.openStream())
            val uuid: String = JsonParser().parse(reader_0).asJsonObject.get("id").asString
            val url_1 = URL("https://sessionserver.mojang.com/session/minecraft/profile/$uuid?unsigned=false")
            val reader_1 = InputStreamReader(url_1.openStream())
            val textureProperty: JsonObject = JsonParser().parse(reader_1).asJsonObject.get("properties").asJsonArray.get(0).asJsonObject
            val texture: String = textureProperty.get("value").asString
            val signature: String = textureProperty.get("signature").asString
            arrayOf(texture, signature)
        } catch (e: IOException) {
            System.err.println("Could not get skin data from session servers!")
            e.printStackTrace()
            null
        }
    }

}