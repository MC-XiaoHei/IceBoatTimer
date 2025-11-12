package cn.xor7.xiaohei.iceBoatTimer

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Location
import java.io.File

object CheckpointManager {
    private val checkpoints = mutableListOf<Checkpoint>()
    private val sectionMap = mutableMapOf<ChunkSection, MutableSet<Checkpoint>>() // 优化索引
    private val json = Json { prettyPrint = true }
    private lateinit var dataFile: File

    fun init(dataFolder: File) {
        dataFile = File(dataFolder, "checkpoints.json")
        load()
    }

    fun addCheckpoint(cp: Checkpoint) {
        checkpoints.add(cp)
        for (section in cp.coveredSections()) {
            sectionMap.computeIfAbsent(section) { mutableSetOf() }.add(cp)
        }
    }

    fun removeCheckpoint(id: String) {
        val toRemove = checkpoints.find { it.id == id } ?: return
        checkpoints.remove(toRemove)
        for (section in toRemove.coveredSections()) {
            sectionMap[section]?.remove(toRemove)
            if (sectionMap[section]?.isEmpty() == true) sectionMap.remove(section)
        }
    }

    fun getAll(): List<Checkpoint> = checkpoints.toList()

    fun findContaining(loc: Location): Checkpoint? {
        val section = ChunkSection(loc.world?.name ?: return null, loc.blockX shr 4, loc.blockZ shr 4)
        val set = sectionMap[section] ?: return null
        return set.firstOrNull { it.contains(loc) }
    }

    fun save() {
        dataFile.writeText(json.encodeToString(checkpoints))
    }

    fun load() {
        checkpoints.clear()
        sectionMap.clear()
        if (dataFile.exists()) {
            val text = dataFile.readText()
            checkpoints.addAll(json.decodeFromString<List<Checkpoint>>(text))
            for (cp in checkpoints) {
                for (section in cp.coveredSections()) {
                    sectionMap.computeIfAbsent(section) { mutableSetOf() }.add(cp)
                }
            }
        }
    }
}