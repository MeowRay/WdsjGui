main();
function main(){
    var servers = "$server".split(",");
    var result = false;
    handler.sendMessage("单价是: $price reason: " + reason);
    if (eco.take(handler.getName(),$price,"POINT",reason)){
        title.sendTitle(handler, "§a§l购买成功");
        result = true;
    } else {
        handler.spigot().sendMessage(Java.type("net.wdsj.servercore.message.ServerMessages$Component").TAKE_POINT_FAIL);
    }
    if (doneClose){
        guimanager.static.close(handler);
    }

    return result;
}