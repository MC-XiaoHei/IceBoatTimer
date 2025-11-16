package cn.xor7.xiaohei.iceBoatTimer.rank

data class NthRankEntry(
    val playerId: String,
    val timeMs: Long,
    val rank: Int,
    val timeDiffMs: Long,
)