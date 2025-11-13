package cn.xor7.xiaohei.iceBoatTimer.utils

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerListener(listener: Listener) {
    server.pluginManager.registerEvents(listener, this)
}