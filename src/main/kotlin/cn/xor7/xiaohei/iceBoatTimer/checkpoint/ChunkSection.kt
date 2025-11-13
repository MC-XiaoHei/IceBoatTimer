package cn.xor7.xiaohei.iceBoatTimer.checkpoint

data class ChunkSection(val world: String, val chunkX: Int, val chunkZ: Int) {
    override fun equals(other: Any?): Boolean {
        return other is ChunkSection &&
                other.world == world &&
                other.chunkX == chunkX &&
                other.chunkZ == chunkZ
    }
    override fun hashCode(): Int = world.hashCode() * 31 * 31 + chunkX * 31 + chunkZ
}