package cn.xor7.xiaohei.iceBoatTimer

import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import cn.xor7.xiaohei.iceBoatTimer.utils.plus
import fr.mrmicky.fastboard.adventure.FastBoard
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.NamedTextColor.BLUE
import net.kyori.adventure.text.format.NamedTextColor.GOLD
import net.kyori.adventure.text.format.NamedTextColor.GRAY
import net.kyori.adventure.text.format.NamedTextColor.GREEN
import net.kyori.adventure.text.format.NamedTextColor.RED
import net.kyori.adventure.text.format.TextDecoration.BOLD
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

object ScoreboardManager : Listener {
    private val boards: MutableMap<UUID, FastBoard> = mutableMapOf()

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.getPlayer()
        this.boards[player.uniqueId] = FastBoard(player).apply {
            updateTitle(text("> 山商MC煤炭社第二届冰船比赛 <", GOLD).decorate(BOLD))
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val player = e.getPlayer()
        this.boards.remove(player.uniqueId)?.delete()
    }

    fun updateAllBoards() {
        boards.values.forEach { it.updateContent() }
    }

    private fun FastBoard.updateContent() {
        val ranking = RecordTimeManager.rankingWindowByNth(player.name, 15)
        for (i in 1..15) {
            val entry = ranking.getOrNull(i - 1)
            if (entry != null) {
                this.updateLine(i - 1, buildRankPart(i) + buildPlayerPart(entry.playerId) + buildTimeDiffPart(entry.timeDiffMs))
            } else {
                this.updateLine(i - 1, text(""))
            }
        }
    }

    private fun buildRankPart(rank: Int) = text(
        when (rank) {
            in 1..9 -> "$rank.  "
            else -> "$rank. "
        },
        GRAY,
    )

    private fun buildPlayerPart(name: String) = text(
        name + " ".repeat(16 - name.length),
        BLUE,
    )

    private fun buildTimeDiffPart(timeDiffMs: Long): TextComponent {
        if (timeDiffMs == 0L) return text("0", GOLD)
        else if (timeDiffMs > 0L) {
            val seconds = timeDiffMs.toDouble() / 1000.0
            return text("+%.3f".format(seconds), GREEN)
        } else {
            val seconds = -timeDiffMs.toDouble() / 1000.0
            return text("-%.3f".format(seconds), RED)
        }
    }
}