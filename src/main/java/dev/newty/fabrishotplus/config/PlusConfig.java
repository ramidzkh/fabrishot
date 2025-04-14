package dev.newty.fabrishotplus.config;

import java.util.Properties;

public class PlusConfig {
    public static boolean SHOW_NAMETAGS = false;

    public static Properties save(Properties properties) {
        properties.put("show_nametags", String.valueOf(SHOW_NAMETAGS));
        return properties;
    }
}
