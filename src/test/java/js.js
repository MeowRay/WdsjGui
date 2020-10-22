
main();
function main(){
    var reason = "$reason";
    var servers = "$server".split(",");
    var result = false;
    if (reason==="$reason"){
        reason = "";
    }
    player.sendMessage(servers);
    player.sendMessage("单价是: $price");
    if (eco.take(player,$price,"POINT",null,reason)){
        player.sendMessage("购买成功");
        result = true;
    } else {
        player.sendMessage("购买失败");
    }
    return result;
}