package eu.koboo.simple.elevator;

import eu.koboo.simple.elevator.config.ElevatorConfig;
import eu.koboo.simple.elevator.listener.PlayerMoveListener;
import eu.koboo.simple.elevator.listener.PlayerToggleSneakListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleElevator extends JavaPlugin {

    private AtomicReference<ElevatorConfig> configReference;

    @Override
    public void onEnable() {
        File pluginDir = getDataFolder();
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }

        File configFile = new File(pluginDir, "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
        configReference = new AtomicReference<>(ElevatorConfig.loadConfig(this));

        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public ElevatorConfig getElevatorConfig() {
        return configReference.get();
    }

    public Location findNextElevatorAbove(Location location) {
        World world = location.getWorld();
        if(world == null) {
            return null;
        }
        return findNextElevator(location, location.getBlockY(), world.getMaxHeight(), true);
    }

    public Location findNextElevatorBelow(Location location) {
        World world = location.getWorld();
        if(world == null) {
            return null;
        }
        return findNextElevator(location, world.getMinHeight(), location.getBlockY(), false);
    }

    public Location findNextElevator(Location location, int from, int to, boolean up) {
        Location tempLoc = null;
        for(int i = from; i <= to; i++) {
            tempLoc = Objects.requireNonNullElseGet(tempLoc, location::clone);
            if(up) {
                tempLoc = tempLoc.add(0, 1, 0);
            } else {
                tempLoc = tempLoc.subtract(0, 1, 0);
            }
            Block block = tempLoc.getBlock();
            if(!getElevatorConfig().elevatorBlockList().contains(block.getType())) {
                continue;
            }
            return tempLoc.add(0, 1, 0);
        }
        return null;
    }
}
