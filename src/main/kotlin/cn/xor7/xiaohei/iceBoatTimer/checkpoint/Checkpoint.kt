package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import kotlinx.serialization.Serializable
import org.bukkit.Location

@Serializable
data class Checkpoint(
    val id: String,
    val num: Int,
    val world: String,
    val minX: Double,
    val minY: Double,
    val minZ: Double,
    val maxX: Double,
    val maxY: Double,
    val maxZ: Double
) {
    fun contains(loc: Location): Boolean {
        if (loc.world?.name != world) return false
        return loc.x in minX..maxX && loc.y in minY..maxY && loc.z in minZ..maxZ
    }
    fun coveredSections(): Set<ChunkSection> {
        val minChunkX = (minX.toInt() shr 4)
        val maxChunkX = (maxX.toInt() shr 4)
        val minChunkZ = (minZ.toInt() shr 4)
        val maxChunkZ = (maxZ.toInt() shr 4)
        val worldName = world
        val set = mutableSetOf<ChunkSection>()
        for (cx in minChunkX..maxChunkX) {
            for (cz in minChunkZ..maxChunkZ) {
                set.add(ChunkSection(worldName, cx, cz))
            }
        }
        return set
    }

    fun overlaps(other: Checkpoint): Boolean {
        if (this.world != other.world) return false
        return this.maxX >= other.minX && this.minX <= other.maxX &&
                this.maxY >= other.minY && this.minY <= other.maxY &&
                this.maxZ >= other.minZ && this.minZ <= other.maxZ
    }
}