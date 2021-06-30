package net.wdsj.mcserver.gui.bukkit.handitem.item

import org.bukkit.entity.Player

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/6/9 21:32
 */
interface CanGrantItem {
    fun givePlayer(player: Player)
}