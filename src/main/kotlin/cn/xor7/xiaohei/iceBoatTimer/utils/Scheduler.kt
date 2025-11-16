package cn.xor7.xiaohei.iceBoatTimer.utils

import cn.xor7.xiaohei.iceBoatTimer.instance

fun runTaskTimer(
    delayTicks: Long,
    periodTicks: Long,
    task: () -> Unit,
) {
    instance.server.scheduler.runTaskTimer(
        instance,
        Runnable { task() },
        delayTicks,
        periodTicks,
    )
}