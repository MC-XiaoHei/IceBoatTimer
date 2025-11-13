package cn.xor7.xiaohei.iceBoatTimer.utils

import net.kyori.adventure.text.Component

operator fun Component.plus(other: Component): Component {
    return this.append(other)
}