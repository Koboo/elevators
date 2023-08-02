package eu.koboo.simple.elevator.config;

import eu.koboo.yaml.converter.Converter;
import org.bukkit.Material;

import java.util.Locale;

public class MaterialConverter implements Converter {

    @Override
    public Class<?>[] compatibleClasses() {
        return new Class[]{Material.class};
    }

    @Override
    public String convertToString(Object o) {
        return ((Material) o).name();
    }

    @Override
    public Material convertToObject(String s) {
        return Material.valueOf(s.toUpperCase(Locale.ROOT));
    }
}
