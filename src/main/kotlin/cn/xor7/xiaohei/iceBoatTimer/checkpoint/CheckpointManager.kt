package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.File

object CheckpointManager {
    private val checkpoints = mutableMapOf<String, Checkpoint>()
    private val sectionMap = mutableMapOf<ChunkSection, MutableSet<Checkpoint>>()
    private val json = Json { prettyPrint = true }
    private lateinit var dataFile: File
    var maxCheckpoint: Int = 1
        private set

    fun init(dataFolder: File) {
        dataFile = File(dataFolder, "checkpoints.json")
        load()
        save()
    }

    fun addCheckpoint(cp: Checkpoint) {
        if (checkpoints.containsKey(cp.id)) throw IllegalArgumentException("检查点ID已存在: ${cp.id}")
        for (section in cp.coveredSections()) {
            val existing = sectionMap.computeIfAbsent(section) { mutableSetOf() }
            val overlaps = existing.firstOrNull { it.overlaps(cp) }
            if (overlaps != null) throw IllegalArgumentException("将要添加的检查点与现有检查点重叠: ${cp.id} 与 ${overlaps.id} 重叠")
            existing.add(cp)
        }
        checkpoints[cp.id] = cp
        recalculateEndNum()
        save()
    }

    fun removeCheckpoint(id: String) {
        val toRemove = checkpoints[id] ?: return
        checkpoints.remove(id)
        for (section in toRemove.coveredSections()) {
            sectionMap[section]?.remove(toRemove)
            if (sectionMap[section]?.isEmpty() == true) sectionMap.remove(section)
        }
        recalculateEndNum()
        save()
    }

    fun getAll(): List<Checkpoint> = checkpoints.map { it.value }

    fun findContaining(loc: Location): Checkpoint? {
        val section = ChunkSection(loc.world?.name ?: return null, loc.blockX shr 4, loc.blockZ shr 4)
        val set = sectionMap[section] ?: return null
        return set.firstOrNull { it.contains(loc) }
    }

    fun save() {
        dataFile.parentFile.mkdirs()
        if (!dataFile.exists()) dataFile.createNewFile()
        dataFile.writeText(json.encodeToString(checkpoints))
    }

    fun load() {
        checkpoints.clear()
        sectionMap.clear()
        if (dataFile.exists()) {
            val text = dataFile.readText()
            checkpoints += json.decodeFromString<Map<String, Checkpoint>>(text)
            for (cp in checkpoints.values) {
                for (section in cp.coveredSections()) {
                    sectionMap.computeIfAbsent(section) { mutableSetOf() }.add(cp)
                }
            }
        }
        recalculateEndNum()
    }

    private fun recalculateEndNum() {
        maxCheckpoint = checkpoints.values.maxOfOrNull { it.num } ?: 0
    }

    fun turnOnParticles() {
        for (cp in checkpoints.values) {
            cp.turnOnParticles()
        }
    }

    fun turnOffParticles() {
        for (cp in checkpoints.values) {
            cp.turnOffParticles()
        }
    }
}