package cn.xor7.xiaohei.iceBoatTimer.game

import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleExitEvent
import org.bukkit.inventory.ItemStack

object GameManager : Listener {
    @EventHandler
    fun onPlayerExitBoat(event: VehicleExitEvent) {
        val player = event.exited as? Player ?: return
        val boat = event.vehicle as? Boat ?: return

        val boatItem = getBoatItemFromEntity(boat)

        player.inventory.addItem(boatItem)
        boat.remove()
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
}