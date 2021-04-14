/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import java.util.*

object TextSplitter {

    @JvmStatic
    fun split(length: Int, lines: List<String>, linePrefix: String, wordSuffix: String): List<String> {
        val builder = StringBuilder()

        for (line in lines) {
            builder.append(line.trim { it <= ' ' })
            builder.append(" ")
        }

        return split(length, builder.substring(0, builder.length - 1), linePrefix, wordSuffix)
    }

    @JvmStatic
    fun split(length: Int, text: String, linePrefix: String, wordSuffix: String): List<String> {
        if (text.length <= length) {
            return arrayListOf(linePrefix + text)
        }

        val lines = ArrayList<String>()
        val split = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var builder = StringBuilder(linePrefix)

        for (i in split.indices) {
            if (builder.length + split[i].length >= length) {
                lines.add(builder.toString())
                builder = StringBuilder(linePrefix)
            }

            builder.append(split[i])
            builder.append(wordSuffix)

            if (i == split.size - 1) {
                builder.replace(builder.length - wordSuffix.length, builder.length, "")
            }
        }

        if (builder.isNotEmpty()) {
            lines.add(builder.toString())
        }

        return lines
    }

}