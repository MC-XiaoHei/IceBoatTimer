package cn.xor7.xiaohei.iceBoatTimer.game

import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.GOLD
import net.kyori.adventure.text.format.NamedTextColor.GREEN
import net.kyori.adventure.title.TitlePart.SUBTITLE
import net.kyori.adventure.title.TitlePart.TITLE
import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.inventory.ItemStack

object GameManager : Listener {
    val startedPlayers = mutableSetOf<String>()

    @EventHandler
    fun onPlayerExitBoat(event: VehicleExitEvent) {
        val player = event.exited as? Player ?: return
        val boat = event.vehicle as? Boat ?: return

        val boatItem = getBoatItemFromEntity(boat)

        player.inventory.addItem(boatItem)
        boat.remove()
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (startedPlayers.contains(player.name)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (startedPlayers.contains(player.name)) return
        player.teleport(player.respawnLocation ?: player.world.spawnLocation)
    }

    private fun getBoatItemFromEntity(boat: Boat): ItemStack {
        val material = when (boat.boatType) {
            Boat.Type.SPRUCE -> Material.SPRUCE_BOAT
            Boat.Type.BIRCH -> Material.BIRCH_BOAT
            Boat.Type.JUNGLE -> Material.JUNGLE_BOAT
            Boat.Type.ACACIA -> Material.ACACIA_BOAT
            Boat.Type.DARK_OAK -> Material.DARK_OAK_BOAT
            Boat.Type.MANGROVE -> Material.MANGROVE_BOAT
            Boat.Type.BAMBOO -> Material.BAMBOO_RAFT
            Boat.Type.CHERRY -> Material.CHERRY_BOAT
            else -> Material.OAK_BOAT
        }
        return ItemStack(material)
    }

    fun playerFinishMatch(player: Player) {
        player.sendTitlePart(TITLE, text("恭喜你完成了比赛！", GOLD))

        val costTime = RecordTimeManager.getCurrentStartedSecs(player.name)
        val timeStr = "%.2f".format(costTime)
        player.sendTitlePart(SUBTITLE, text("用时 $timeStr 秒", GREEN))
    }
}