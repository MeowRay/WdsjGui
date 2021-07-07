package net.wdsj.mcserver.gui.bukkit.command

import net.wdsj.mcserver.gui.common.command.GuiLabelCommand
import net.wdsj.mcserver.gui.common.wrapper.CanOpenItem
import net.wdsj.servercore.common.SimpleParamParse
import net.wdsj.servercore.common.command.anntations.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author  MeowRay
 * @date  2021/7/7 19:26
 * @version 1.0
 */
class GuiBukkitLabelCommand(val item: CanOpenItem) :GuiLabelCommand<CommandSender> {

    @SubCommand(help = "")
    fun main(sender: Player, args: Array<String>) {
        if (item.requirementCanOpen(sender)) {
            val map: MutableMap<String, String> = HashMap()
            if (args.size > 1) for ((key, value) in SimpleParamParse.parse(args[1], ";", "=").map) {
                map["$$key"] = value
            }
            item.open(sender, map)
        }
    }

}