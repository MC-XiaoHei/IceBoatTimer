package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import cn.xor7.xiaohei.iceBoatTimer.utils.plus
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.Location
import java.util.*

private val playerSelections = mutableMapOf<UUID, Pair<Location?, Location?>>()

fun registerCheckpointCommand() = commandTree("checkpoint") {
    withPermission("iceboattimer.checkpoint.manage")
    literalArgument("pos1") {
        withAliases("p1")
        playerExecutor { player, _ ->
            val pair = playerSelections[player.uniqueId] ?: (null to null)
            playerSelections[player.uniqueId] = player.location to pair.second
            player.sendMessage(
                text(
                    "已设置第一个点: ",
                    GREEN,
                ) + text(
                    "${player.location.blockX}, ${player.location.blockY}, ${player.location.blockZ}",
                    YELLOW,
                ),
            )
        }
    }
    literalArgument("pos2") {
        withAliases("p2")
        playerExecutor { player, _ ->
            val pair = playerSelections[player.uniqueId] ?: (null to null)
            playerSelections[player.uniqueId] = pair.first to player.location
            player.sendMessage(
                text(
                    "已设置第二个点: ",
                    GREEN,
                ) + text(
                    "${player.location.blockX}, ${player.location.blockY}, ${player.location.blockZ}",
                    YELLOW,
                ),
            )
        }
    }
    literalArgument("add") {
        stringArgument("id") {
            integerArgument("num") {
                booleanArgument("isFinal", true) {
                    playerExecutor { player, args ->
                        val id: String by args
                        val num: Int by args
                        val isFinal: Boolean? by args
                        val (loc1, loc2) = playerSelections[player.uniqueId] ?: (null to null)
                        if (loc1 == null || loc2 == null) {
                            player.sendMessage(text("请先用 /checkpoint pos1 和 /checkpoint pos2 选点！", RED))
                            return@playerExecutor
                        }
                        if (loc1.world != loc2.world) {
                            player.sendMessage(text("两个点必须在同一个世界！", RED))
                            return@playerExecutor
                        }
                        val minX = minOf(loc1.x, loc2.x).toInt()
                        val minY = minOf(loc1.y, loc2.y).toInt()
                        val minZ = minOf(loc1.z, loc2.z).toInt()
                        val maxX = maxOf(loc1.x, loc2.x).toInt()
                        val maxY = maxOf(loc1.y, loc2.y).toInt()
                        val maxZ = maxOf(loc1.z, loc2.z).toInt()
                        val world = loc1.world?.name ?: run {
                            player.sendMessage(text("世界名获取失败！", RED))
                            return@playerExecutor
                        }
                        val cp = Checkpoint(id, num, world, minX, minY, minZ, maxX, maxY, maxZ, isFinal ?: false)
                        try {
                            CheckpointManager.addCheckpoint(cp)
                        } catch (e: IllegalArgumentException) {
                            player.sendMessage(text("添加检查点失败: ${e.message}", RED))
                            return@playerExecutor
                        }
                        player.sendMessage(
                            text(
                                "检查点 $id 已添加. 区域: ",
                                GREEN,
                            ) + text(
                                "($minX, $minY, $minZ) ~ ($maxX, $maxY, $maxZ)",
                                YELLOW,
                            ),
                        )
                    }
                }
            }
        }
    }
    literalArgument("remove") {
        stringArgument("id") {
            playerExecutor { player, args ->
                val id = args.getUnchecked<String>("id") ?: run {
                    player.sendMessage(text("ID不能为空！", RED))
                    return@playerExecutor
                }
                CheckpointManager.removeCheckpoint(id)
                player.sendMessage(text("检查点 $id 已移除.", GREEN))
            }
        }
    }
    literalArgument("list") {
        playerExecutor { player, _ ->
            val checkpoints = CheckpointManager.getAll()
            if (checkpoints.isEmpty()) {
                player.sendMessage(text("没有检查点！", RED))
                return@playerExecutor
            }
            player.sendMessage(text("当前检查点列表:", GREEN))
            for (cp in checkpoints) {
                player.sendMessage(
                    text(
                        "${cp.id} - ${cp.num} (${cp.world}) [${cp.minX}, ${cp.minY}, ${cp.minZ}] ~ [${cp.maxX}, ${cp.maxY}, ${cp.maxZ}]",
                        YELLOW,
                    ),
                )
            }
        }
    }
    literalArgument("particles") {
        literalArgument("off") {
            anyExecutor { _, _ ->
                CheckpointManager.turnOffParticles()
            }
        }
        literalArgument("on") {
            anyExecutor { _, _ ->
                CheckpointManager.turnOnParticles()
            }
        }
    }
}
