/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.method

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.command.Command
import cc.fyre.stark.engine.command.CommandHandler
import cc.fyre.stark.engine.command.CommandNode
import cc.fyre.stark.engine.command.data.Data
import cc.fyre.stark.engine.command.data.flag.Flag
import cc.fyre.stark.engine.command.data.flag.FlagData
import cc.fyre.stark.engine.command.data.parameter.Param
import cc.fyre.stark.engine.command.data.parameter.ParameterData
import cc.fyre.stark.engine.command.data.processor.Processor
import org.bukkit.command.CommandSender
import java.lang.reflect.Method

class MethodProcessor : Processor<Method, Set<CommandNode>?> {

    override fun process(type: Method): Set<CommandNode>? {
        if (type.isAnnotationPresent(Command::class.java)) {
            if (type.parameterCount >= 1 && CommandSender::class.java.isAssignableFrom(type.parameterTypes[0])) {
                val command = type.getAnnotation(Command::class.java)
                val owningClass = type.declaringClass
                val flagNames = mutableListOf<String>()
                val allParams = mutableListOf<Data>()

                if (type.parameterCount > 1) {
                    for (i in 1 until type.parameterCount) {
                        val parameter = type.parameters[i]

                        if (parameter.isAnnotationPresent(Param::class.java)) {
                            val param: Param = parameter.getAnnotation(Param::class.java)
                            val hash = setOf(*param.tabCompleteFlags)
                            val data = ParameterData(param.name, param.defaultValue, parameter.type, param.wildcard, i, hash, Stark.instance.commandHandler.parameterTypeMap[parameter.type]?.javaClass)
                            allParams.add(data)
                        } else {
                            if (!parameter.isAnnotationPresent(Flag::class.java)) {
                                throw IllegalArgumentException("Every data, other than the sender, must be annotated with Param")
                            }

                            val flag: Flag = parameter.getAnnotation(Flag::class.java)
                            val flagData = FlagData(listOf(*flag.value), flag.description, flag.defaultValue, i)

                            allParams.add(flagData)
                            flagNames.addAll(listOf(*flag.value))
                        }
                    }
                }

                val registered = HashSet<CommandNode>()

                for (name in command.names) {
                    val qualifiedName = name.toLowerCase().trim()
                    var hadChild = false
                    var cmdNames: Array<String> = arrayOf(qualifiedName)

                    if (qualifiedName.contains(" ")) {
                        cmdNames = qualifiedName.split(" ").toTypedArray()
                    }

                    val primaryName = cmdNames[0]
                    var workingNode = CommandNode(owningClass)

                    if (CommandHandler.rootNode.hasCommand(primaryName)) {
                        workingNode = CommandHandler.rootNode.getCommand(primaryName)!!
                        workingNode.aliases.add(primaryName)
                    } else {
                        workingNode.name = primaryName
                    }

                    var parentNode = CommandNode(owningClass)

                    if (workingNode.hasCommand(primaryName)) {
                        parentNode = workingNode.getCommand(primaryName)!!
                    } else {
                        parentNode.name = primaryName
                        parentNode.permission = ""
                    }

                    if (cmdNames.size > 1) {
                        hadChild = true

                        workingNode.registerCommand(parentNode)

                        var childNode = CommandNode(owningClass)

                        for (i in 1 until cmdNames.size) {
                            val subName = cmdNames[i]

                            childNode.name = subName

                            if (parentNode.hasCommand(subName)) {
                                childNode = parentNode.getCommand(subName)!!
                            }

                            parentNode.registerCommand(childNode)

                            if (i == cmdNames.size - 1) {
                                childNode.method = type
                                childNode.async = command.async
                                childNode.hidden = command.hidden
                                childNode.permission = command.permission
                                childNode.description = command.description
                                childNode.validFlags = flagNames
                                childNode.parameters = allParams
                                childNode.logToConsole = command.logToConsole
                            } else {
                                parentNode = childNode
                                childNode = CommandNode(owningClass)
                            }
                        }
                    }

                    if (!hadChild) {
                        parentNode.method = type
                        parentNode.async = command.async
                        parentNode.hidden = command.hidden
                        parentNode.permission = command.permission
                        parentNode.description = command.description
                        parentNode.validFlags = flagNames
                        parentNode.parameters = allParams
                        parentNode.logToConsole = command.logToConsole

                        workingNode.registerCommand(parentNode)
                    }

                    CommandHandler.rootNode.registerCommand(workingNode)
                    registered.add(workingNode)
                }

                return registered
            }
        }

        return null
    }

}