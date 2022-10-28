package eu.koboo.simple.elevator.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public record ElevatorConfig(List<Material> elevatorBlockList) {

    public static ElevatorConfig loadConfig(JavaPlugin plugin) {
        List<Material> elevatorBlockList = checkAndGetMaterialList(plugin, "settings.elevator-blocks",
                Arrays.asList(Material.LAPIS_BLOCK, Material.REDSTONE_BLOCK));

        return new ElevatorConfig(elevatorBlockList);
    }

    private static int checkAndGetIntValue(JavaPlugin plugin, String key, int fallbackVal) {
        return checkAndGetValue(plugin, key, fallbackVal, () -> plugin.getConfig().getInt(key));
    }

    private static List<Material> checkAndGetMaterialList(JavaPlugin plugin, String key, List<Material> fallbackVal) {
        return checkAndGetValue(plugin, key, fallbackVal, () -> {
            List<String> materialStringList = plugin.getConfig().getStringList(key);
            if (materialStringList.isEmpty()) {
                return fallbackVal;
            }
            List<Material> materialList = new ArrayList<>();
            for (String materialName : materialStringList) {
                try {
                    Material material = Material.valueOf(materialName.toUpperCase(Locale.ROOT));
                    materialList.add(material);
                } catch (Exception e) {
                    plugin.getLogger().info("Config error: Invalid material \"" + materialName + "\" found in list for key \"" + key + "\"!");
                    return fallbackVal;
                }
            }
            return materialList;
        });
    }

    private static <T> T checkAndGetValue(JavaPlugin plugin, String key, T fallbackVal, Supplier<T> getter) {
        FileConfiguration config = plugin.getConfig();
        String typeClass = fallbackVal.getClass().getSimpleName();
        if (!config.contains(key)) {
            plugin.getLogger().info("Config error: No key \"" + key + "\" (valueType = " + typeClass + ")!");
            return fallbackVal;
        }
        try {
            T configValue = getter.get();
            if (configValue == null) {
                plugin.getLogger().info("Config error: key \"" + key + "\" has value (valueType = " + typeClass + ")!");
                return fallbackVal;
            }
            return getter.get();
        } catch (Exception e) {
            plugin.getLogger().info("Config error: Invalid " + typeClass + " for key \"" + key + "\"!");
            return fallbackVal;
        }
    }
}
