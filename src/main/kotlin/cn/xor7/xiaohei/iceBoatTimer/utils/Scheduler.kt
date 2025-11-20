package cn.xor7.xiaohei.iceBoatTimer.utils

import cn.xor7.xiaohei.iceBoatTimer.instance
import org.bukkit.scheduler.BukkitTask

fun runTaskTimer(
    delayTicks: Long,
    periodTicks: Long,
    task: BukkitTask.() -> Unit,
) {
    var bukkitTask: BukkitTask? = null
    bukkitTask = instance.server.scheduler.runTaskTimer(
        instance,
        Runnable { bukkitTask?.task() },
        delayTicks,
        periodTicks,
    )
}