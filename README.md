# WdsjGui
支持bungeecord&amp;spigot基于封包的gui插件


spigot: 1.7-1.16
bungeecord: all

**仅供学习使用!禁止商用,如二次开发需注明出处**

服务器: wdsj.net

支持各种自定义模板，传参，灵活应用轻松制作商店等等功能

## 特性
* 继承
* 模板
* 动态
* 忘了

## 演示

### Menu:
![alt 喵喵喵?](https://github.com/MeowRay/WdsjGui/raw/main/demo.gif)

### Item:
![alt 喵喵喵?](https://github.com/MeowRay/WdsjGui/blob/main/ITEM.gif)

### Sign:
![alt 喵喵喵?](https://github.com/MeowRay/WdsjGui/raw/main/SIGN.gif)

## 配置
### config.yml
```
model-pane3x9:
  title: '菜单'
  layout:
    - '========='
    - '=       ='
    - '========[CLOSE]'
  
  items:
    =:
      repo: PLACEHOLDER_BLACK_GLASS_PANE
    CLOSE:
      repo: QUIT_MENU_ARROW
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
  #设置父菜单
  parent: 'model-pane5x9'
  #继承
  type: 'INHERIT'
  #只需要设置要用的
  layout:
    - ''
    - '    BDCA'
    - ''
  construct: '
    if(player.isOp()){
      menu.setTitle(menu.getTitle()+" 23333")
    }
  '
  #快速容器
  container:
    test:
      #第三排第一个到第八个，可跨行可设置多个,分割
      range: '3:1-3:8'
      items:
        - display:
            - material: GLASS
              name: '§eNO.1'
        - display:
            - material: GLASS
              name: '§eNO.2'
  items:
    C:
      update: 2  # 2 tick更新一次
      display:
        - head: 'Pink 1'
          name: '§eHello! %player_name%'  #设置名字，接下来的display自动继承，直到设置为其他内容或者null(不继承
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
          name: '§e点击购买草方块 $price$'  #引用参数price
          lore:
            - '§clore2'
            - '§e现在的时间是: %localtime_time_yyyy-MM-dd-HH:mm:ss%'
      action:
        left:
          - 'cmd:console:give %player_name% GRASS 1'  #如模板传回true则继续执行..
      args:
        price: '10'
        reason: '购买草方块'
    D:
      update: 5
      model: 'item' #使用item购买的模板
      display:
        - material: STONE
          name: '§e$reason'
          lore:
            - '§clore1'
            - '§e现在的时间是: %localtime_time_yyyy-MM-dd-HH:mm:ss%'
        - material: STONE
          enchant:
            - DAMAGE_ALL:1
          name: '§e$reason'
          lore:
            - '§clore2'
            - '§e现在的时间是: %localtime_time_yyyy-MM-dd-HH:mm:ss%'
      action:
        left:
          - 'cmd:console:give %player_name% STONE 1'
      #传参
      args:
        priceDisplay: "§e需要 {price}"
        items:
          - material: GRASS_BLOCK
            amount: 128
            lore:
              - 'need lore'
        reason: '购买石头'
    TTTT:
      update: 5
      #将在以下slot动态显示
      slot:
        - 10
        - 11
        - 12
      display:
        - material: GRASS_BLOCK
          name: '这是1'
      #满足条件显示
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
### model.yml
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
    doneClose: true
  action:
    left:
      - 'js:
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
      '
item:
  display:
    - material: STONE
  requirement-args:
    - reason
    - items
    - number
    - priceDisplay
  args:
    reason: ''
    server: ''
    number: 1
    doneClose: 'true'
    priceDisplay: '§6{price}' // 默认
  initScript:
    - '
   init();
   function init(){
       print(items);
       var itemHandler = eco.getEcoHandler("ITEM");
       if(itemHandler!=null){
         var data = eco.createEcoData(itemHandler, utils.createSectionByList(items,"items"));
         print(data);
       }

       if(data !=null ){
         for each (var el in render.getDisplay()){
           el.getLore().add(priceDisplay.replace("{price}",data.getPriceDisplay() ));
         }

         options.put("BUY_ITEM_MODEL_DATA", data);
      }
   }

    '
  action:
    left:
      - 'js:
        main();
        function main(){
            var data = options.get("BUY_ITEM_MODEL_DATA");

           var itemHandler = eco.getEcoHandler("ITEM");
           if(data == null){
             handler.sendMessage("系统错误！请联系腐竹~");
             return false;
           }
            var result = false;
            if (eco.take(handler.getName(),itemHandler,data,1,null,reason)){
                title.sendTitles(handler, "§a§l购买成功");
                result = true;
            } else {
                title.sendTitles(handler, "§c§l物资不足" , "§6你需要: "+data.getPriceDisplay());
            }
            if (doneClose){
                guimanager.static.close(handler);
            }

            return result;
        }
      '
```
