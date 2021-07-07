package net.wdsj.mcserver.gui.bukkit.command

import net.wdsj.mcserver.gui.common.command.GuiLabelController
import net.wdsj.mcserver.gui.common.wrapper.CanOpenItem
import net.wdsj.servercore.common.command.CommandProxyBuilder
import net.wdsj.servercore.utils.extensions.CommandProxyHolder
import net.wdsj.servercore.utils.extensions.registerCommand
import net.wdsj.servercore.utils.extensions.unregisterAllCommand
import net.wdsj.servercore.utils.extensions.unregisterCommand
import org.bukkit.plugin.Plugin

class GuiLabelBukkitController(private val plugin: Plugin) : GuiLabelController, CommandProxyHolder {

    private val labelMap: MutableMap<String, CommandProxyBuilder<*>> = mutableMapOf()

    override fun register(label: String, item: CanOpenItem) {
        val builder = CommandProxyBuilder.newBuilder(plugin, GuiBukkitLabelCommand(item)).apply { setLabel(label) }
        labelMap[label] = builder
        registerCommand(builder, force = true)
    }

    override fun unregister(label: String) {
        labelMap.remove(label)?.let { unregisterCommand(it) }
    }

    override fun unregisterAll() {
        labelMap.clear()
        unregisterAllCommand()
    }
}