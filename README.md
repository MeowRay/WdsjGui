# WdsjGui
支持bungeecord&amp;spigot基于封包的gui插件


spigot: 1.7-1.16
bungeecord: all

**仅供学习使用!**

服务器: wdsj.net

## 特性
* 继承
* 模板
* 动态
* 忘了

## 演示

![alt 喵喵喵?](https://github.com/MeowRay/WdsjGui/blob/main/demo.gif)

## 配置
### config.yml
```
hub-main-menu:
  type: INJECT
  items:
    39:
      slot:
        - 39
      display:
        - material: BOOK
          name: §3游戏任务
          lore:
            - '§7点击查看当前游戏任务'
      action:
        left:
          - 'cmd:player:rw'
    40:
      slot:
        - 40
      display:
        - material: EXPERIENCE_BOTTLE
          name: §3游戏成就
          lore:
            - '§7点击查看当前游戏成就'
      action:
        left:
          - 'cmd:player:cj'
test:
  title: 'DEMO MENU :)'
  parent: model-pane3x9
  type: INHERIT
  layout:
    - ''
    - '    B CA'
    - ''
  construct: '
    if(player.isOp()){
      menu.setTitle(menu.getTitle()+" 23333")
    }
  '
  items:
    C:
      update: 2
      display:
        - head: 'Pink 1'
          name: '§eHello! %player_name%'
        - head: 'Yellow 2'
        - head: 'Blue 3'
        - head: 'Green 4'
    A:
      update: 5
      display:
        - material: APPLE
          name: '§ename1'
          lore:
            - '§clore1'
        - material: APPLE
          name: '§ename2'
          lore:
            - '§clore2'
          enchant:
            - DAMAGE_ALL:1
          flag:
            - HIDE_ENCHANTS
      action:
        all:
          - 'close'
          - 'cmd:console:say hello'
    B:
      update: 5
      model: 'point'
      display:
        - material: GRASS_BLOCK
          name: '§eBUY A GRASS BLOCK $price$'
          lore:
            - '§clore1'
            - '§e现在的时间是: %localtime_time_yyyy-MM-dd-HH:mm:ss%'
        - material: DIRT
          name: '§e点击购买草方块 $price$'
          lore:
            - '§clore2'
            - '§e现在的时间是: %localtime_time_yyyy-MM-dd-HH:mm:ss%'
      action:
        left:
          - 'cmd:console:give %player_name% GRASS 1'
      args:
        price: '10'
        reason: '购买草方块'
    TTTT:
      update: 5
      slot:
        - 10
        - 11
        - 12
      display:
        - material: GRASS_BLOCK
          name: '这是1'
      display-condition:
        - condition: '"%player_name%" === "Ya_Mo_TAT"'
          item:
            - material: WOODEN_SWORD
              name: '你是OP啊啊'
            - material: STONE_SWORD
              name: '你是OP啊啊'
            - material: DIAMOND_SWORD
              name: '你是OP啊啊'
```
model.yml
```
point:
  display:
    - material: STONE
  requirement-args:
    - price
    - reason
  args:
    reason: ''
    server: ''
    doneClose: 'true'
  action:
    left:
      - 'js:
        main();
        function main(){
            var reason = "$reason";
            var doneClose = "$doneClose";
            var servers = "$server".split(",");
            var result = false;
            handler.sendMessage("单价是: $price reason: " + reason);
            if (eco.take(handler.getName(),$price,"POINT",reason)){
                title.sendTitle(handler, "§a§l购买成功");
                result = true;
            } else {
                handler.spigot().sendMessage(Java.type("net.wdsj.servercore.message.ServerMessages$Component").TAKE_POINT_FAIL);
            }
            if (doneClose === "true"){
                guimanager.static.close(handler);
            }

            return result;
        }
      '
```
