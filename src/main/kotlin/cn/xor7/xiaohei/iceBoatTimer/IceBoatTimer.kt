package cn.xor7.xiaohei.iceBoatTimer

import cn.xor7.xiaohei.iceBoatTimer.checkpoint.CheckpointListener
import cn.xor7.xiaohei.iceBoatTimer.checkpoint.CheckpointManager
import cn.xor7.xiaohei.iceBoatTimer.checkpoint.registerCheckpointCommand
import cn.xor7.xiaohei.iceBoatTimer.game.GameManager
import cn.xor7.xiaohei.iceBoatTimer.game.registerGameCommand
import cn.xor7.xiaohei.iceBoatTimer.rank.RecordTimeManager
import cn.xor7.xiaohei.iceBoatTimer.rank.registerRecordTimeCommand
import cn.xor7.xiaohei.iceBoatTimer.scoreboard.ScoreboardManager
import cn.xor7.xiaohei.iceBoatTimer.spawn.SpawnAreaManager
import cn.xor7.xiaohei.iceBoatTimer.spawn.registerSpawnAreaCommand
import cn.xor7.xiaohei.iceBoatTimer.utils.registerListener
import cn.xor7.xiaohei.iceBoatTimer.utils.runTaskTimer
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPISpigotConfig
import org.bukkit.plugin.java.JavaPlugin

lateinit var instance: IceBoatTimer

class IceBoatTimer : JavaPlugin() {
    override fun onLoad() {
        instance = this
        CommandAPI.onLoad(CommandAPISpigotConfig(this))
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        SpawnAreaManager.init(dataFolder)
        CheckpointManager.init(dataFolder)
        RecordTimeManager.init(dataFolder)
        registerCheckpointCommand()
        registerRecordTimeCommand()
        registerSpawnAreaCommand()
        registerGameCommand()
        registerListener(CheckpointListener)
        registerListener(ScoreboardManager)
        registerListener(GameManager)
        runTaskTimer(0, 1) { ScoreboardManager.updateAllBoards() }
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        CheckpointManager.save()
        RecordTimeManager.save()
    }
}
