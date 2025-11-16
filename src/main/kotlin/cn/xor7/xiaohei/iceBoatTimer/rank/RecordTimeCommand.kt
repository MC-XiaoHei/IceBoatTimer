package cn.xor7.xiaohei.iceBoatTimer.rank

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.entitySelectorArgumentOnePlayer
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.literalArgument
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.RED
import org.bukkit.entity.Player

fun registerRecordTimeCommand() = commandTree("record-time") {
    literalArgument("clear") {
        literalArgument("confirm") {
            anyExecutor { sender, _ ->
                RecordTimeManager.clearAll()
                sender.sendMessage(text("已清除所有计时记录！", RED))
            }
        }
        entitySelectorArgumentOnePlayer("player") {
            literalArgument("confirm") {
                anyExecutor { sender, args ->
                    val player: Player by args
                    RecordTimeManager.clearPlayer(player.name)
                    sender.sendMessage(text("已删除玩家 ${player.name} 的计时记录！", RED))
                }
            }
        }
    }
}