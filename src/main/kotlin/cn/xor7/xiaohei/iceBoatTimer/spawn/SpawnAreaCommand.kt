package cn.xor7.xiaohei.iceBoatTimer.spawn

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor

fun registerSpawnAreaCommand() = commandTree("spawn-area") {
    withPermission("iceboattimer.spawn-area.manage")
    literalArgument("pos1") {
        playerExecutor { player, _ ->
            SpawnAreaManager.setP1(player.location)
        }
    }
    literalArgument("pos2") {
        playerExecutor { player, _ ->
            SpawnAreaManager.setP2(player.location)
        }
    }
    literalArgument("particles") {
        literalArgument("off") {
            anyExecutor { _, _ ->
                SpawnAreaManager.turnOffParticles()
            }
        }
        literalArgument("on") {
            anyExecutor { _, _ ->
                SpawnAreaManager.turnOnParticles()
            }
        }
    }
}
