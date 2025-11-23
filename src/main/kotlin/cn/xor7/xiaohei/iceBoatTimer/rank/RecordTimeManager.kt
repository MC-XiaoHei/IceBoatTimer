package cn.xor7.xiaohei.iceBoatTimer.rank

import cn.xor7.xiaohei.iceBoatTimer.checkpoint.CheckpointManager.maxCheckpoint
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object RecordTimeManager {
    private val records: MutableMap<String, PlayerRecord> = mutableMapOf()
    private val json = Json { prettyPrint = true }
    private lateinit var dataFile: File

    fun init(dataFolder: File) {
        dataFile = File(dataFolder, "record-times.json")
        load()
        save()
    }

    fun resetPlayerStartTime(playerId: String) {
        val record = records.computeIfAbsent(playerId) { PlayerRecord() }
        records[playerId] = PlayerRecord(checkpoints = record.checkpoints)
        save()
    }

    fun setTimeIfAbsent(playerId: String, checkpoint: Int, timeMs: Long) {
        val record = records.computeIfAbsent(playerId) { PlayerRecord() }
        if (!record.checkpoints.containsKey(checkpoint)) {
            record.checkpoints[checkpoint] = timeMs - record.startTimeMills
            save()
        }
    }

    fun getCurrentStartedSecs(playerId: String): Double {
        val record = records[playerId] ?: return 0.0
        val diff = System.currentTimeMillis() - record.startTimeMills
        return diff / 1000.0
    }

    fun clearPlayer(playerId: String) {
        records.remove(playerId)
        save()
    }

    fun clearAll() {
        records.clear()
        save()
    }

    fun save() {
        dataFile.parentFile?.mkdirs()
        if (!dataFile.exists()) dataFile.createNewFile()
        dataFile.writeText(json.encodeToString(records))
    }

    private fun load() {
        records.clear()
        if (!::dataFile.isInitialized) return
        if (!dataFile.exists()) return
        val text = dataFile.readText().ifBlank { return }
        val loaded: MutableMap<String, PlayerRecord> = try {
            json.decodeFromString(text)
        } catch (e: Exception) {
            e.printStackTrace()
            mutableMapOf()
        }
        records += loaded
    }

    fun rankingByNth(playerId: String): List<NthRankEntry> {
        val targetMax = records[playerId]?.checkpoints?.keys?.maxOrNull() ?: return emptyList()
        if (targetMax > maxCheckpoint) return emptyList()

        val targetRecord = records[playerId] ?: return emptyList()
        val targetCheckpoints = targetRecord.checkpoints

        val targetSearchEnd = minOf(targetCheckpoints.keys.maxOrNull() ?: 0, maxCheckpoint)
        var targetFoundCheckpoint: Int? = null
        for (k in targetMax..targetSearchEnd) {
            if (targetCheckpoints.containsKey(k)) {
                targetFoundCheckpoint = k
                break
            }
        }

        val targetPlayerTime = targetCheckpoints[targetFoundCheckpoint] ?: return emptyList()

        val tempResults = mutableListOf<Pair<String, Long>>()
        for ((pid, record) in records) {
            val checkpoints = record.checkpoints
            val playerMax = checkpoints.keys.maxOrNull() ?: continue
            if (playerMax < targetMax) continue

            val searchEnd = minOf(playerMax, maxCheckpoint)
            var foundCheckpoint: Int? = null
            for (k in targetMax..searchEnd) {
                if (checkpoints.containsKey(k)) {
                    foundCheckpoint = k
                    break
                }
            }
            if (foundCheckpoint == null) continue
            val time = checkpoints[foundCheckpoint] ?: continue

            tempResults.add(pid to time)
        }

        val sortedResults = tempResults.sortedWith(compareBy({ it.second }, { it.first }))

        return sortedResults.mapIndexed { index, (pid, time) ->
            NthRankEntry(
                rank = index + 1,
                playerId = pid,
                timeMs = time,
                timeDiffMs = time - targetPlayerTime
            )
        }
    }

    fun rankingWindowByNth(playerId: String, windowSize: Int): List<NthRankEntry> {
        if (windowSize <= 0) return emptyList()

        val full = rankingByNth(playerId)
        if (full.size <= windowSize) return full

        val idx = full.indexOfFirst { it.playerId == playerId }
        if (idx == -1) {
            return full.subList(0, windowSize)
        }

        val half = windowSize / 2
        var start = idx - half
        if (start < 0) start = 0
        if (start + windowSize > full.size) start = full.size - windowSize

        return full.subList(start, start + windowSize)
    }
}
