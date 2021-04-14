/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import com.google.common.collect.ImmutableSet
import org.bukkit.plugin.Plugin
import org.reflections.util.ClasspathHelper
import org.reflections.vfs.Vfs
import java.util.*

object ClassUtils {
    @JvmStatic
    fun getClassesInPackage(plugin: Plugin, packageName: String): Collection<Class<*>> {
        val classes = HashSet<Class<*>>()
        for (url in ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader(), plugin.javaClass.classLoader)) {
            val dir = Vfs.fromURL(url)
            try {
                for (file in dir.files) {
                    val name = file.relativePath.replace("/", ".").replace(".class", "")
                    if (name.startsWith(packageName)) {
                        classes.add(Class.forName(name))
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                dir.close()
            }
        }
        return ImmutableSet.copyOf(classes)
    }
}