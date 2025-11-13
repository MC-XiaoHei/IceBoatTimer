package cn.xor7.xiaohei.iceBoatTimer.checkpoint

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object CheckpointListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val location = event.to
        val checkpoints = CheckpointManager.findContaining(location) ?: return

        player.sendMessage(
            "你进入了检查点: ${checkpoints.id} (编号: ${checkpoints.num}) " +
            "在世界 ${checkpoints.world} 的位置: " +
            "${location.blockX}, ${location.blockY}, ${location.blockZ}"
        )
    }
}