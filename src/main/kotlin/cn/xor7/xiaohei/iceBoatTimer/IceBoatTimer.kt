package cn.xor7.xiaohei.iceBoatTimer

import org.bukkit.plugin.java.JavaPlugin

class IceBoatTimer : JavaPlugin() {

    override fun onEnable() {
        CheckpointManager.init(dataFolder)
        CheckpointEditCommand.register()
        // Plugin startup logic
    }

    override fun onDisable() {
        CheckpointManager.save()
        // Plugin shutdown logic
    }
}
