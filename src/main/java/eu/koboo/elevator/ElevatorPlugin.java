package eu.koboo.elevator;

import eu.koboo.elevator.config.Config;
import eu.koboo.elevator.config.MaterialConverter;
import eu.koboo.elevator.listener.PlayerMoveListener;
import eu.koboo.elevator.listener.PlayerToggleSneakListener;
import eu.koboo.yaml.YamlInstance;
import eu.koboo.yaml.migration.YamlMigration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;
import java.util.Objects;

@Getter
@Plugin(name = "PROJECT_NAME", version = "PROJECT_VERSION")
@ApiVersion(ApiVersion.Target.v1_13)
@Author("PROJECT_GROUP")
@Description("PROJECT_DESCRIPTION")
@LoadOrder(PluginLoadOrder.POSTWORLD)
@Website("PROJECT_WEBSITE")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElevatorPlugin extends JavaPlugin {

    private static final int MIN_HEIGHT = -64;

    private static final int BSTATS_ID = 19365;

    Config elevatorConfig;

    @Override
    public void onEnable() {
        YamlInstance.getConverter().registerConverter(new MaterialConverter());

        getDataFolder().mkdirs();
        YamlMigration migration = new YamlMigration();
        elevatorConfig = migration.migrateConfig(
            Config.class,
            new File(getDataFolder(), "config.yml"),
            "config-version",
            true,
            true
        );

        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public Location findNextElevatorAbove(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return null;
        }
        return findNextElevator(location, location.getBlockY(), world.getMaxHeight(), true);
    }

    public Location findNextElevatorBelow(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return null;
        }
        return findNextElevator(location, MIN_HEIGHT, location.getBlockY(), false);
    }

    public Location findNextElevator(Location location, int from, int to, boolean up) {
        Location tempLoc = null;
        for (int i = from; i <= to; i++) {
            tempLoc = Objects.requireNonNullElseGet(tempLoc, location::clone);
            if (up) {
                tempLoc = tempLoc.add(0, 1, 0);
            } else {
                tempLoc = tempLoc.subtract(0, 1, 0);
            }
            Block block = tempLoc.getBlock();
            if (!elevatorConfig.getElevatorMaterials().contains(block.getType())) {
                continue;
            }
            return tempLoc.add(0, 1, 0);
        }
        return null;
    }
}
