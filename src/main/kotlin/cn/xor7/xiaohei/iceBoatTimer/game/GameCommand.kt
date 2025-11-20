package cn.xor7.xiaohei.iceBoatTimer.game

import cn.xor7.xiaohei.iceBoatTimer.utils.runTaskTimer
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.BLUE
import net.kyori.adventure.title.TitlePart.TITLE
import org.bukkit.entity.Player

fun registerGameCommand() = commandTree("game") {
    literalArgument("prepare") {
        stringArgument("player") {
            anyExecutor { sender, args ->
                val player: String by args

            }
        }
    }
    literalArgument("start") {
        entitySelectorArgumentOnePlayer("player") {
            playerExecutor { _, args ->
                val player: Player by args
                var countdown = 10
                runTaskTimer(0, 20) {
                    if (countdown > 0) {
                        player.sendTitlePart(TITLE, text("$countdown", BLUE))
                        countdown--
                    } else {
                        player.sendTitlePart(TITLE, text("开始！", BLUE))
                        cancel()
                    }
                }
            }
        }
    }
}