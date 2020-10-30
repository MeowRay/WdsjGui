package net.wdsj.mcserver.gui.common;

import com.google.common.base.Enums;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import net.wdsj.common.simpleconfig.ConfigurationSection;
import net.wdsj.common.simpleconfig.file.YamlConfiguration;
import net.wdsj.mcserver.gui.common.config.GuiItemModelRenderConfig;
import net.wdsj.mcserver.gui.common.config.GuiItemRenderConfig;
import net.wdsj.mcserver.gui.common.config.GuiItemShowConfig;
import net.wdsj.mcserver.gui.common.config.GuiItemStackConfig;
import net.wdsj.mcserver.gui.common.creator.GuiItemConfigCreator;
import net.wdsj.mcserver.gui.common.creator.GuiItemExecutorCreator;
import net.wdsj.mcserver.gui.common.creator.GuiSignExecutorCreator;
import net.wdsj.mcserver.gui.common.execption.GuiItemCreatorNotFoundException;
import net.wdsj.mcserver.gui.common.executor.GuiItemExecutor;
import net.wdsj.mcserver.gui.common.executor.GuiItemExecutorCollection;
import net.wdsj.mcserver.gui.common.item.GuiItem;
import net.wdsj.mcserver.gui.common.item.GuiItemBase;
import net.wdsj.mcserver.gui.common.repo.GuiItemRepository;
import net.wdsj.mcserver.gui.common.utils.MenuUtils;
import net.wdsj.mcserver.gui.common.wrapper.GuiMenuConfigWrapper;
import net.wdsj.mcserver.gui.common.wrapper.GuiSignConfigWrapper;
import net.wdsj.servercore.WdsjServerAPI;
import net.wdsj.servercore.config.invoke.ConfigInvoke;
import net.wdsj.servercore.database.frame.box.value.bytes.ymal.DatabaseBytesConfigValue;
import net.wdsj.servercore.eunm.inventory.InventoryAction;
import net.wdsj.servercore.utils.ArrayUtils;
import net.wdsj.servercore.utils.ReflectionUtils;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Arthur
 * @version 1.0
 * @date 2018/8/27 23:17
 */
public class GuiConfigManager {

    private static final Logger LOGGER = Logger.getLogger("GuiMenuConfigManager");

    private static final Map<String, GuiItemModelRenderConfig> itemModelMap = new THashMap<>();

    private static final Map<String, GuiItemExecutorCreator<?>> menuExecutorCreatorMap = new THashMap<>();

    private static final Map<String, GuiSignExecutorCreator<?>> signExecutorCreatorMap = new THashMap<>();

    private static final Map<String, GuiItemConfigCreator<?, ?>> itemCreatorMap = new THashMap<>();

    private static final Map<String, GuiMenuConfigWrapper> menuMap = new HashMap<>();

    private static final Map<String, GuiSignConfigWrapper> signMap = new HashMap<>();

    public static void registerMenu(String name, GuiMenuConfigWrapper config) {
        menuMap.put(name, config);
    }

    public static void registerSign(String name, GuiSignConfigWrapper config) {
        signMap.put(name, config);
    }

    public static GuiMenuConfigWrapper getGuiMenu(String name) {
        return menuMap.get(name);
    }

    public static GuiSignConfigWrapper getGuiSign(String name) {
        return signMap.get(name);
    }

    public static void init() {
        itemModelMap.clear();
        menuMap.clear();
        YamlConfiguration menuYaml = WdsjServerAPI.getConfigManager().readServerGroup("Gui#Menu", new DatabaseBytesConfigValue());
        YamlConfiguration menuGlobalYaml = WdsjServerAPI.getConfigManager().readMain("Gui#Menu", new DatabaseBytesConfigValue());
        YamlConfiguration modelYaml = WdsjServerAPI.getConfigManager().readServerGroup("Gui#Model", new DatabaseBytesConfigValue());
        YamlConfiguration modelGlobalYaml = WdsjServerAPI.getConfigManager().readMain("Gui#Model", new DatabaseBytesConfigValue());
        YamlConfiguration signGlobalYaml = WdsjServerAPI.getConfigManager().readMain("Gui#Sign", new DatabaseBytesConfigValue());
        YamlConfiguration signYaml = WdsjServerAPI.getConfigManager().readServerGroup("Gui#Sign", new DatabaseBytesConfigValue());
        if (modelGlobalYaml != null) {
            int i = loadModelFromYaml(modelGlobalYaml);
            LOGGER.info(String.format("加载了全局模板: %d个", i));
        }
        if (modelYaml != null) {
            int i = loadModelFromYaml(modelYaml);
            LOGGER.info(String.format("加载了模板: %d个", i));
        }
        if (menuGlobalYaml != null) {
            int i = loadMenuFromYaml(menuGlobalYaml);
            LOGGER.info(String.format("加载了全局菜单: %d个", i));
        }
        if (menuYaml != null) {
            int i = loadMenuFromYaml(menuYaml);
            LOGGER.info(String.format("加载了菜单: %d个", i));
        }
        if (signGlobalYaml != null) {
            int i = loadSignFromYaml(signGlobalYaml);
            LOGGER.info(String.format("加载了全局木牌: %d个", i));
        }
        if (signYaml != null) {
            int i = loadSignFromYaml(signYaml);
            LOGGER.info(String.format("加载了木牌: %d个", i));
        }
    }

    public static int loadModelFromYaml(ConfigurationSection section) {
        int n = 0;
        for (String key : section.getKeys(false)) {
            GuiItemModelRenderConfig config = ConfigInvoke.invoke(GuiItemModelRenderConfig.class, section.getConfigurationSection(key));
            addItemModelRenderConfig(key, config);
            n++;
        }
        return n;
    }

    public static int loadMenuFromYaml(ConfigurationSection section) {
        int n = 0;
        for (String key : section.getKeys(false)) {
            GuiMenuConfigWrapper guiMenuConfigWrapper = new GuiMenuConfigWrapper(section.getConfigurationSection(key));
            registerMenu(key, guiMenuConfigWrapper);
            n++;
        }
        return n;
    }

    public static int loadSignFromYaml(ConfigurationSection section) {
        int n = 0;
        for (String key : section.getKeys(false)) {
            GuiSignConfigWrapper wrapper = new GuiSignConfigWrapper(section.getConfigurationSection(key));
            registerSign(key, wrapper);
            n++;
        }
        return n;
    }

    public static void setMergeModelRenderConfig(String key, GuiItemRenderConfig originConfig) {
        setMergeModelRenderConfig(itemModelMap.get(key), originConfig);
    }


    public static void setMergeModelRenderConfig(GuiItemModelRenderConfig modelConfig, GuiItemRenderConfig originConfig) {
        if (modelConfig != null) {
            for (Map.Entry<String, Object> entry : modelConfig.getArgs().entrySet()) {
                if (!originConfig.getArgs().containsKey(entry.getKey())) {
                    originConfig.getArgs().put(entry.getKey(), entry.getValue());
                }
            }
            if (originConfig.getDisplay().isEmpty() && !modelConfig.getDisplay().isEmpty()) {
                originConfig.getDisplay().addAll(modelConfig.getDisplay());
            }

            if (!originConfig.getOptions().containsKey("OVERRIDE_MODEL")) {
                for (Map.Entry<String, ArrayList<String>> entry : modelConfig.getAction().entrySet()) {
                    List<String> strings = originConfig.getAction().getOrDefault(entry.getKey() , new ArrayList<>());
                    strings.addAll(0, entry.getValue());
                    originConfig.getAction().put(entry.getKey(), strings);
                }
                if (!modelConfig.getInitScript().isEmpty()) {
                    originConfig.getInitScript().addAll(modelConfig.getInitScript());
                }
                originConfig.getOptions().put("OVERRIDE_MODEL", true);
            }

            for (String arg : modelConfig.getRequirementArgs()) {
                if (!originConfig.getArgs().containsKey(arg)) {
                    throw new RuntimeException(String.format("Arg '%s' is require", arg));
                }
            }
        }
    }
    public static void addItemModelRenderConfig(String key, GuiItemModelRenderConfig config) {
        itemModelMap.put(key, config);
    }

    public static void registerMenuExecutorCreator(String key, GuiItemExecutorCreator<?> creator) {
        menuExecutorCreatorMap.put(key, creator);
    }

    public static void registerSignExecutorCreator(String key, GuiSignExecutorCreator<?> creator) {
        signExecutorCreatorMap.put(key, creator);
    }

    public static GuiItemExecutorCreator<?> getMenuExecutorCreator(String key) {
        return menuExecutorCreatorMap.get(key);
    }

    public static GuiSignExecutorCreator<?> getSignExecutorCreator(String key) {
        return signExecutorCreatorMap.get(key);
    }

    public static <Handler> List<GuiItem<?, ?>> getGuiItemRender(Handler handler, GuiItemRenderConfig config) {
        if (WdsjServerAPI.getServerType().isBukkit()) {
            return getGuiItemRender(getItemCreator("BUKKIT"), handler, config);
        } else if (WdsjServerAPI.getServerType().isBungee()) {
            return getGuiItemRender(getItemCreator("BUNGEE"), handler, config);
        }
        throw new GuiItemCreatorNotFoundException("default");
    }


    //TODO
    private static final Set<GuiItemRenderConfig> renderOverrideConfigSet = new THashSet<>();

    public static <Handler> List<GuiItem<?, ?>> getGuiItemRender(GuiItemConfigCreator<Handler, ?> guiItemConfigCreator, Handler handler, GuiItemRenderConfig config) {
        List<GuiItem<?, ?>> items = new ArrayList<>();
       /* if (config.getModel() != null) {
            setMergeModelRenderConfig(itemModelMap.get(config.getModel()), config);
        } else*/
        if (config.getRepo() != null) {
            GuiItem guiItem = GuiItemRepository.get(config.getRepo());
            if (guiItem != null) {
                items.add(guiItem);
            }
        }


        if (handler != null) {
            for (GuiItemShowConfig guiItemShowConfig : config.getDisplayCondition()) {
                String condition = MenuUtils.replace(guiItemShowConfig.getCondition(), config.getStringArgs());
                if (Strings.isNullOrEmpty(condition) || MenuUtils.scriptExecute(condition, handler, false)) {
                    GuiItemStackConfig preConfig = null;
                    for (int i = 0; i < guiItemShowConfig.getItem().size(); i++) {
                        GuiItemStackConfig guiItemStackConfig = guiItemShowConfig.getItem().get(i);
                        if (preConfig != null) {
                            ReflectionUtils.copyFieldsOnlyNull(preConfig, guiItemStackConfig);
                        }
                        GuiItem<Handler, ?> playerItemStackGuiItem = guiItemConfigCreator.create(handler, config.getStringArgs(), guiItemStackConfig);
                        items.add(playerItemStackGuiItem);
                        preConfig = guiItemStackConfig;
                    }
                }
            }
        }


        if (items.isEmpty()) {
            boolean override = renderOverrideConfigSet.contains(config);
            if (!override) {
                renderOverrideConfigSet.add(config);
            }
            GuiItemStackConfig preConfig = null;
            for (int i = 0; i < config.getDisplay().size(); i++) {
                GuiItemStackConfig guiItemStackConfig = config.getDisplay().get(i);
                if (!override && preConfig != null) {
                    ReflectionUtils.copyFieldsOnlyNull(preConfig, guiItemStackConfig);
                }
                GuiItem<?, ?> playerItemStackGuiItem = guiItemConfigCreator.create(handler, config.getStringArgs(), guiItemStackConfig);
                items.add(playerItemStackGuiItem);
                preConfig = guiItemStackConfig;
            }

        }
        for (GuiItem<?, ?> item : items) {
            setGuiItemAction(item, config.getAction(), config.getArgs());
        }
        return items;
    }

    public static <H, I> GuiItem<H, I> setGuiItemAction(GuiItem<H, I> guiItem, Map<String, List<String>> actionMap, Map<String, Object> argsMap) {
        if (guiItem instanceof GuiItemBase) {
            GuiItemBase<H, I> guiItemBase = (GuiItemBase<H, I>) guiItem;
            for (Map.Entry<String, List<String>> entry : actionMap.entrySet()) {
                String key = entry.getKey();
                Optional<InventoryAction> inventoryActionOptional = Enums.getIfPresent(InventoryAction.class, key.toUpperCase());
                if (inventoryActionOptional.isPresent()) {
                    List<String> value = entry.getValue();
                    GuiItemExecutorCollection<H> executorCollection = new GuiItemExecutorCollection<>();
                    for (String s : value) {
                        Optional<GuiItemExecutor<Object>> guiItemExecutor = getGuiItemExecutorCreator(s, argsMap);
                        if (guiItemExecutor.isPresent()) {
                            executorCollection.addExecutor((GuiItemExecutor<H>) guiItemExecutor.get());
                        }
                    }
                    guiItemBase.addActionExecutor(inventoryActionOptional.get(), executorCollection);
                }
            }
        }
        return guiItem;
    }

    public static <Handler> Optional<GuiItemExecutor<Handler>> getGuiItemExecutorCreator(String text, Map<String, Object> argsMap) {
        String[] split = text.split(":", 2);
        GuiItemExecutorCreator<?> actionCreator = getMenuExecutorCreator(split[0]);
        if (actionCreator != null) {
            String s = ArrayUtils.get(split, 1);
            return Optional.of((GuiItemExecutor<Handler>) actionCreator.create(s, argsMap));
        }
        return Optional.absent();
    }

    public static void registerItemCreator(String key, GuiItemConfigCreator<?, ?> creator) {
        itemCreatorMap.put(key, creator);
    }

    public static <Handler, Item> GuiItemConfigCreator<Handler, Item> getItemCreator(String key) {
        return (GuiItemConfigCreator<Handler, Item>) itemCreatorMap.get(key);
    }


}
