package cn.xor7.xiaohei.iceBoatTimer.game

import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import cn.xor7.xiaohei.iceBoatTimer.spawn.SpawnAreaManager
import cn.xor7.xiaohei.iceBoatTimer.utils.runTaskTimer
import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.BLUE
import net.kyori.adventure.title.TitlePart.TITLE
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun registerGameCommand() = commandTree("game") {
    literalArgument("start") {
        entitySelectorArgumentOnePlayer("player") {
            playerExecutor { _, args ->
                val player: Player by args
                var countdown = 10
                player.teleport(SpawnAreaManager.getSpawnLocation())
                player.inventory.clear()
                player.inventory.addItem(ItemStack(Material.OAK_BOAT))
                runTaskTimer(0, 20) {
                    if (countdown > 0) {
                        player.sendTitlePart(TITLE, text("$countdown", BLUE))
                    } else if (countdown == 0) {
                        player.sendTitlePart(TITLE, text("开始！", BLUE))
                        GameManager.startedPlayers += player.name
                        RecordTimeManager.resetPlayerStartTime(player.name)
                    } else {
                        player.sendTitlePart(TITLE, text(""))
                        cancel()
                    }
                    countdown--
                }
            }
        }
    }
}