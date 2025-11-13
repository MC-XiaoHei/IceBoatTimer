package cn.xor7.xiaohei.iceBoatTimer

import cn.xor7.xiaohei.iceBoatTimer.checkpoint.CheckpointListener
import cn.xor7.xiaohei.iceBoatTimer.checkpoint.CheckpointManager
import cn.xor7.xiaohei.iceBoatTimer.checkpoint.registerCheckpointEditCommand
import cn.xor7.xiaohei.iceBoatTimer.utils.registerListener
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPISpigotConfig
import org.bukkit.plugin.java.JavaPlugin

class IceBoatTimer : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPISpigotConfig(this))
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        CheckpointManager.init(dataFolder)
        registerCheckpointEditCommand()
        registerListener(CheckpointListener)
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        CheckpointManager.save()
    }
}
