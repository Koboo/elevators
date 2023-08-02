package eu.koboo.elevator.config;

import eu.koboo.yaml.config.YamlComment;
import eu.koboo.yaml.config.YamlKey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Config {

    @YamlKey("config-version")
    @YamlComment("!! Do not touch this or it will break your config file !!")
    int version = 1;

    @YamlKey("elevator-materials")
    @YamlComment({
        "Define the materials, which act as elevator here.",
        "See this list: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html"
    })
    List<Material> elevatorMaterials = Arrays.asList(Material.LAPIS_BLOCK, Material.REDSTONE_BLOCK);
}
