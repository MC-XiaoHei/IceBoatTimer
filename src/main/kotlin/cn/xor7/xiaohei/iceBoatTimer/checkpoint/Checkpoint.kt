package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.bukkit.Bukkit
import org.bukkit.Location
import top.zoyn.particlelib.pobject.Cube

@Serializable
data class Checkpoint(
    val id: String,
    val num: Int,
    val world: String,
    val minX: Int,
    val minY: Int,
    val minZ: Int,
    val maxX: Int,
    val maxY: Int,
    val maxZ: Int
) {
    @Transient
    private val particle = run {
        val world = Bukkit.getWorld(this.world)
        val loc1 = Location(world, this.minX.toDouble(), this.minY.toDouble(), this.minZ.toDouble())
        val loc2 = Location(world, this.maxX.toDouble(), this.maxY.toDouble(), this.maxZ.toDouble())
        Cube(loc1, loc2).apply {
            period = 1L
            step = 0.5
        }
    }

    fun contains(loc: Location): Boolean {
        if (loc.world?.name != world) return false
        return loc.x.toInt() in minX..maxX && loc.y.toInt() in minY..maxY && loc.z.toInt() in minZ..maxZ
    }
    fun coveredSections(): Set<ChunkSection> {
        val minChunkX = (minX shr 4)
        val maxChunkX = (maxX shr 4)
        val minChunkZ = (minZ shr 4)
        val maxChunkZ = (maxZ shr 4)
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

    fun turnOnParticles() {
        particle.alwaysShowAsync()
    }

    fun turnOffParticles() {
        particle.turnOffTask()
    }
}