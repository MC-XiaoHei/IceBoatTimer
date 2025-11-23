package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import cn.xor7.xiaohei.iceBoatTimer.game.GameManager
import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object CheckpointListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val location = event.to
        val checkpoint = CheckpointManager.findContaining(location) ?: return
        RecordTimeManager.setTimeIfAbsent(player.name, checkpoint.num, System.currentTimeMillis())

        if (checkpoint.isFinal) GameManager.playerFinishMatch(player)
    }
}