package eu.koboo.elevator.listener;

import eu.koboo.elevator.ElevatorPlugin;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PlayerToggleSneakListener implements Listener {

    ElevatorPlugin plugin;

    @EventHandler
    @SuppressWarnings("unused")
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking()) {
            return;
        }
        Block fromBlock = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!plugin.getElevatorConfig().getElevatorMaterials().contains(fromBlock.getType())) {
            return;
        }
        // Player wants to go down
        Location elevatorLoc = plugin.findNextElevatorBelow(player.getLocation().subtract(0, 1.5, 0));
        if (elevatorLoc == null) {
            return;
        }
        player.teleport(elevatorLoc);
        player.playSound(elevatorLoc, Sound.ENTITY_BAT_TAKEOFF, 2f, 1f);
    }
}
