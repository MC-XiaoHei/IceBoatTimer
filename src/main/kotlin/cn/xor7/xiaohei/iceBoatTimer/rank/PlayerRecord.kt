package cn.xor7.xiaohei.iceBoatTimer.rank

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRecord(
    val checkpoints: MutableMap<Int, Long> = mutableMapOf(),
    val startTimeMills: Long = System.currentTimeMillis(),
)