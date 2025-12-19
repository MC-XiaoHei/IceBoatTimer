package cn.xor7.xiaohei.iceBoatTimer.scoreboard

import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import cn.xor7.xiaohei.iceBoatTimer.utils.plus
import fr.mrmicky.fastboard.adventure.FastBoard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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
            updateTitle(
                text("> 山商MC煤炭社第二届冰船比赛 <", NamedTextColor.GOLD).decorate(TextDecoration.BOLD),
            )
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
        val windowSize = 15
        val ranking = RecordTimeManager.rankingWindowByNth(player.name, windowSize)
        val currentTimeSecs = RecordTimeManager.getCurrentStartedSecs(player.name)
        for (i in 1..windowSize) {
            val entry = ranking.getOrNull(i - 1)
            if (entry != null) {
                this.updateLine(
                    i - 1,
                    buildRankPart(entry.rank) + buildPlayerPart(entry.playerId) + buildTimeDiffPart(
                        entry.timeDiffMs,
                        currentTimeSecs,
                    ),
                )
            } else {
                this.updateLine(i - 1, text("".repeat(25)))
            }
        }
    }

    private fun buildRankPart(rank: Int) = text(
        when (rank) {
            in 1..9 -> "$rank.  "
            else -> "$rank. "
        },
        NamedTextColor.GRAY,
    )

    private fun buildPlayerPart(name: String) = text(
        name + " ".repeat(20 - name.length),
        NamedTextColor.BLUE,
    )

    private fun buildTimeDiffPart(timeDiffMs: Long, currentTimeSecs: Double): Component {
        if (timeDiffMs == 0L) return text("%9.2f".format(currentTimeSecs), NamedTextColor.BLUE) + text(
            "s",
            NamedTextColor.GRAY,
        )
        val seconds = timeDiffMs.toDouble() / 1000.0
        val secsStr = "%+.2f".format(seconds)
        val spaceNum = 7 - secsStr.length
        val color = if (timeDiffMs < 0L) NamedTextColor.RED else NamedTextColor.GREEN
        return text(" ".repeat(spaceNum) + secsStr, color) +
                text("s", NamedTextColor.GRAY)
    }
}