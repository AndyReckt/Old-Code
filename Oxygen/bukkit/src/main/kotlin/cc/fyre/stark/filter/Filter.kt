/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.filter

import cc.fyre.stark.util.StringUtil.IP_REGEX
import cc.fyre.stark.util.StringUtil.URL_REGEX
import com.google.common.collect.ImmutableList
import java.util.regex.Matcher

class Filter {

    companion object {
        private val FILTERED_PHRASES: List<String> = ImmutableList.of(
                "ddos", "aids", "cheats", "cheating", "cheat ", " cheat", "hack", "reach",
                "swat", "e t b", "dox", "porn", "nigger", "chink"
        )

        private val SINGLE_FILTERED_WORDS = arrayOf("L", "#", "etb", "kys", "nigger", "chink", "hax")
        private val WHITELISTED_LINKS = arrayOf(
                "ovidhcf.com", "youtube.com", "youtu.be", "imgur.com", "prntscr.com", "prnt.sc", "gfycat.com", "gyazo.com",
                "twitter.com", "spotify.com", "twitch.tv", "tinypic.com"
        )

        init {
            val words = arrayOf("fuck", "shit", "ass", "garbage", "horrible", "nigger")
            val matches = arrayOf("kb", "knockback", "server", "staff", "pots")
        }
    }

    fun isFiltered(msg: String): Boolean {
        var msg = msg
        msg = msg.toLowerCase().trim { it <= ' ' }
        for (word in msg.split(" ".toRegex()).toTypedArray()) {
            val matcher: Matcher = IP_REGEX.matcher(word)
            if (matcher.matches()) {
                return true
            }
        }
        for (word in msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()) {
            val matcher: Matcher = URL_REGEX.matcher(word)
            var filtered = false
            if (matcher.matches()) {
                var matches = 0
                for (link in WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++
                    }
                }
                filtered = matches == 0
            }
            if (filtered) {
                return true
            }
        }
        for (word in msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replace(" ", "")
                .trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()) {
            val matcher: Matcher = URL_REGEX.matcher(word)
            var filtered = false
            if (matcher.matches()) {
                var matches = 0
                for (link in WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++
                    }
                }
                filtered = matches == 0
            }
            if (filtered) {
                return true
            }
        }
        for (word in msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim { it <= ' ' }.replace("\\p{Punct}|\\d".toRegex(), ":").replace(":dot:", ".").split(" ".toRegex()).toTypedArray()) {
            val matcher: Matcher = URL_REGEX.matcher(word)
            var filtered = false
            if (matcher.matches()) {
                var matches = 0
                for (link in WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++
                    }
                }
                filtered = matches == 0
            }
            if (filtered) {
                return true
            }
        }
        for (word in msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replace(" ", "")
                .trim { it <= ' ' }.replace("\\p{Punct}|\\d".toRegex(), ":").replace(":dot:", ".").split(" ".toRegex()).toTypedArray()) {
            val matcher: Matcher = URL_REGEX.matcher(word)
            var filtered = false
            if (matcher.matches()) {
                var matches = 0
                for (link in WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++
                    }
                }
                filtered = matches == 0
            }
            if (filtered) {
                return true
            }
        }
        val parsed = msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim { it <= ' ' }
        val noPuncParsed = parsed.replace("\\p{Punct}|\\d".toRegex(), "").trim { it <= ' ' }
        for (word in SINGLE_FILTERED_WORDS) {
            if (noPuncParsed.equals(word, ignoreCase = true) || noPuncParsed.startsWith("$word ")
                    || noPuncParsed.endsWith(" $word") || noPuncParsed.contains(" $word ")) {
                return true
            }
        }
        for (phrase in FILTERED_PHRASES) {
            if (parsed.contains(phrase)) {
                return true
            }
        }
        val filterablePhrase = FILTERED_PHRASES.stream().map { phrase: String -> phrase.replace(" ".toRegex(), "") }.filter { s: String? -> parsed.contains(s!!) }.findFirst()
        if (filterablePhrase.isPresent) {
            return true
        }
        val split = parsed.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
        for (word in split) {
            if (FILTERED_PHRASES.contains(word)) {
                return true
            }
        }
        return false
    }
}