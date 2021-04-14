/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command

import cc.fyre.stark.core.tags.Tag
import cc.fyre.stark.core.tags.TagType
import cc.fyre.stark.core.util.mojanguser.MojangUser
import cc.fyre.stark.engine.command.bukkit.ExtendedCommand
import cc.fyre.stark.engine.command.bukkit.ExtendedCommandMap
import cc.fyre.stark.engine.command.bukkit.ExtendedHelpTopic
import cc.fyre.stark.engine.command.data.method.MethodProcessor
import cc.fyre.stark.engine.command.data.parameter.ParameterType
import cc.fyre.stark.engine.command.data.parameter.impl.*
import cc.fyre.stark.engine.command.data.parameter.impl.filter.NormalFilter
import cc.fyre.stark.engine.command.data.parameter.impl.filter.StrictFilter
import cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapper
import cc.fyre.stark.engine.command.data.parameter.impl.offlineplayer.OfflinePlayerWrapperParameterType
import cc.fyre.stark.engine.command.defaults.commands.admin.*
import cc.fyre.stark.engine.command.defaults.commands.other.*
import cc.fyre.stark.engine.command.defaults.commands.player.CraftCommand
import cc.fyre.stark.engine.command.defaults.commands.player.HealCommand
import cc.fyre.stark.engine.command.defaults.commands.player.NightVisionCommand
import cc.fyre.stark.engine.command.defaults.commands.staff.*
import cc.fyre.stark.profile.punishment.command.create.KickCommand
import cc.fyre.stark.tags.commands.TagParameterType
import cc.fyre.stark.tags.commands.TagTypeParameterType
import cc.fyre.stark.tags.commands.TagsCommands
import cc.fyre.stark.util.ClassUtils
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.SimpleCommandMap
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

class CommandHandler {

    companion object {
        val rootNode: CommandNode = CommandNode()
    }

    val parameterTypeMap: MutableMap<Class<*>, ParameterType<*>> = HashMap()
    private val commandMap: CommandMap
    private val knownCommands: MutableMap<String, Command>

    init {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        commandMap = commandMapField.get(Bukkit.getServer()) as CommandMap

        val knownCommandsField = commandMap.javaClass.getDeclaredField("knownCommands")
        knownCommandsField.isAccessible = true

        knownCommands = knownCommandsField.get(commandMap) as MutableMap<String, Command>
    }

    fun load() {
        registerParameterType(Boolean::class.java, BooleanParameterType())
        registerParameterType(Integer::class.java, IntegerParameterType())
        registerParameterType(Int::class.java, IntegerParameterType())
        registerParameterType(Double::class.java, DoubleParameterType())
        registerParameterType(Float::class.java, FloatParameterType())
        registerParameterType(String::class.java, StringParameterType())
        registerParameterType(GameMode::class.java, GameModeParameterType())
        registerParameterType(EntityType::class.java, EntityTypeParameterType())
        registerParameterType(Player::class.java, PlayerParameterType())
        registerParameterType(World::class.java, WorldParameterType())
        registerParameterType(ItemStack::class.java, ItemStackParameterType())
        registerParameterType(Enchantment::class.java, EnchantmentParameterType())
        registerParameterType(OfflinePlayer::class.java, OfflinePlayerParameterType())
        registerParameterType(OfflinePlayerWrapper::class.java, OfflinePlayerWrapperParameterType())
        registerParameterType(MojangUser::class.java, MojangUserParameterType())
        registerParameterType(UUID::class.java, UUIDParameterType())
        registerParameterType(NormalFilter::class.java, NormalFilter())
        registerParameterType(StrictFilter::class.java, StrictFilter())
        registerParameterType(TagType::class.java, TagTypeParameterType())
        registerParameterType(Tag::class.java, TagParameterType())

        registerClass(BroadcastCommand::class.java)
        registerClass(BuildCommand::class.java)
        registerClass(ClearCommand::class.java)
        registerClass(CraftCommand::class.java)
        registerClass(EnchantCommand::class.java)
        registerClass(FeedCommand::class.java)
        registerClass(FlyCommand::class.java)
        registerClass(FreezeCommand::class.java)
        registerClass(GamemodeCommands::class.java)
        registerClass(HeadCommand::class.java)
        registerClass(HealCommand::class.java)
        registerClass(InvseeCommand::class.java)
        registerClass(KickCommand::class.java)
        registerClass(KillCommand::class.java)
        registerClass(ListCommand::class.java)
        registerClass(MoreCommand::class.java)
        registerClass(PingCommand::class.java)
        registerClass(RenameCommand::class.java)
        registerClass(RepairCommand::class.java)
        registerClass(SetSlotsCommand::class.java)
        registerClass(SetSpawnCommand::class.java)
        registerClass(SpawnerCommand::class.java)
        registerClass(SpeedCommand::class.java)
        registerClass(SudoCommands::class.java)
        registerClass(TeleportationCommands::class.java)
        registerClass(UptimeCommand::class.java)
        registerClass(WorldCommand::class.java)
        registerClass(DisguiseCommand::class.java)
        registerClass(RawCommand::class.java)
        registerClass(SpawnCommand::class.java)
        registerClass(GiveCommand::class.java)
        registerClass(KillAllCommand::class.java)
       // registerClass(HideStaffCommand::class.java)
        registerClass(SeenCommand::class.java)
        registerClass(CopyInvCommand::class.java)
        registerClass(CopyToCommand::class.java)
        registerClass(NightVisionCommand::class.java)

        // Tags
        registerClass(TagsCommands::class.java)

        swapCommandMap()
    }

    fun registerParameterType(clazz: Class<*>, parameterType: ParameterType<*>) {
        parameterTypeMap[clazz] = parameterType
    }

    fun getParameterType(clazz: Class<*>): ParameterType<*>? {
        return parameterTypeMap[clazz]
    }

    private fun swapCommandMap() {
        val commandMapField = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        commandMapField.isAccessible = true

        val oldCommandMap = commandMapField.get(Bukkit.getServer())
        val newCommandMap = ExtendedCommandMap(Bukkit.getServer())

        val knownCommandsField = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
        knownCommandsField.isAccessible = true

        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(knownCommandsField, knownCommandsField.modifiers and -0x11)

        knownCommandsField.set(newCommandMap, knownCommandsField.get(oldCommandMap))
        commandMapField.set(Bukkit.getServer(), newCommandMap)
    }

    fun registerClass(clazz: Class<*>) {
        for (method in clazz.methods) {
            registerMethod(method)
        }
    }

    private fun registerMethod(method: Method) {
        method.isAccessible = true

        val nodes = MethodProcessor().process(method)
        if (nodes != null) {
            nodes.forEach { node ->
                val command = ExtendedCommand(node, JavaPlugin.getProvidingPlugin(method.declaringClass))

                register(command)

                node.children.values.forEach { child ->
                    registerHelpTopic(child, node.aliases)
                }
            }
        }
    }

    private fun register(command: ExtendedCommand) {
        val iterator = knownCommands.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.name.equals(command.name, true)) {
                entry.value.unregister(commandMap)
                iterator.remove()
            }
        }

        command.aliases.forEach { alias ->
            knownCommands[alias] = command
        }

        command.register(commandMap)
        knownCommands[command.name] = command
    }

    private fun registerHelpTopic(node: CommandNode, aliases: Set<String>?) {
        if (node.method != null) {
            Bukkit.getHelpMap().addTopic(ExtendedHelpTopic(node, aliases))
        }

        if (node.hasCommands()) {
            node.children.values.forEach { child ->
                registerHelpTopic(child, null)
            }
        }
    }

    fun registerPackage(plugin: Plugin, packageName: String) {
        ClassUtils.getClassesInPackage(plugin, packageName).forEach(this::registerClass)
    }

    fun registerAll(plugin: Plugin) {
        registerPackage(plugin, plugin::class.java.`package`.name)
    }

}