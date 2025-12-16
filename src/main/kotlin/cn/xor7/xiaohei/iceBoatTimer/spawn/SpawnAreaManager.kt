package cn.xor7.xiaohei.iceBoatTimer.spawn

import cn.xor7.xiaohei.iceBoatTimer.checkpoint.Checkpoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Bukkit
import org.bukkit.Location
import java.io.File

object SpawnAreaManager {
    private val json = Json { prettyPrint = true }
    private lateinit var dataFile: File
    private var spawnArea = Checkpoint("spawn", 0, "world", 0, 0, 0, 0, 0, 0, false)

    fun init(dataFolder: File) {
        dataFile = File(dataFolder, "spawn-area.json")
        load()
        save()
    }

    fun save() {
        dataFile.parentFile.mkdirs()
        if (!dataFile.exists()) dataFile.createNewFile()
        dataFile.writeText(json.encodeToString(spawnArea))
    }

    fun load() {
        if (!dataFile.exists()) return
        val text = dataFile.readText()
        spawnArea = json.decodeFromString<Checkpoint>(text)
    }

    fun setP1(loc: Location) {
        spawnArea = spawnArea.copy(
            maxX = loc.x.toInt(),
            maxY = loc.y.toInt(),
            maxZ = loc.z.toInt(),
            world = loc.world?.name ?: "overworld",
        )
        save()
    }

    fun setP2(loc: Location) {
        spawnArea = spawnArea.copy(
            minX = loc.x.toInt(),
            minY = loc.y.toInt(),
            minZ = loc.z.toInt(),
            world = loc.world?.name ?: "overworld",
        )
        save()
    }

    fun turnOnParticles() {
        spawnArea.turnOnParticles()
    }

    fun turnOffParticles() {
        spawnArea.turnOffParticles()
    }

    fun inSpawnArea(location: Location) = spawnArea.contains(location)

    fun getSpawnLocation(): Location {
        return Location(
            Bukkit.getWorld(spawnArea.world),
            (spawnArea.maxX + spawnArea.minX) / 2.0,
            (spawnArea.maxY + spawnArea.minY) / 2.0,
            (spawnArea.maxZ + spawnArea.minZ) / 2.0,
        )
    }
}