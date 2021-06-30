package net.wdsj.mcserver.gui.common.config

import mc233.cn.wdsjlib.global.config.ItemStackConfig
import java.util.*

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/6/12 23:17
 */
data class ExecutorItemConfig(
    var item: ItemStackConfig = ItemStackConfig(),
    var action: Map<String, List<String>> = emptyMap(),
    var requirement: String = "true",
    var uniqueId :String = UUID.randomUUID().toString(),
    var canDrop : Boolean = false,
    var canMove : Boolean = false,
    var slot : Int = 0,
){

}