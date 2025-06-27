package me.ramidzkh.fabrishot.config;

import java.util.Locale;

public enum FileFormat {

    PNG,
    JPG,
    TGA,
    BMP,
    ;

    public String extension() {
        return "." + name().toLowerCase(Locale.ROOT);
    }
}
