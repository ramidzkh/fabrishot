/*
 * MIT License
 *
 * Copyright (c) 2021 Ramid Khan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ramidzkh.fabrishot.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import me.ramidzkh.fabrishot.Fabrishot;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class ClothConfigBridge implements ConfigScreenFactory<Screen> {

    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(new TranslatableText("fabrishot.config.title"))
                .setSavingRunnable(() -> {
                    Properties properties = new Properties();
                    properties.put("override_screenshot_key", String.valueOf(Config.OVERRIDE_SCREENSHOT_KEY));
                    properties.put("width", String.valueOf(Config.CAPTURE_WIDTH));
                    properties.put("height", String.valueOf(Config.CAPTURE_HEIGHT));

                    try (BufferedWriter writer = Files.newBufferedWriter(FabricLoader.getInstance().getConfigDir().resolve("fabrishot.properties"))) {
                        properties.store(writer, "Fabrishot");
                    } catch (IOException exception) {
                        LogManager.getLogger(Fabrishot.class).error(exception.getMessage(), exception);
                    }
                })
                .setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("fabrishot.config.category"));

        category.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("fabrishot.config.override_screenshot_key"), Config.OVERRIDE_SCREENSHOT_KEY)
                .setDefaultValue(false)
                .setSaveConsumer(b -> Config.OVERRIDE_SCREENSHOT_KEY = b)
                .build());

        category.addEntry(entryBuilder.startIntField(new TranslatableText("fabrishot.config.width"), Config.CAPTURE_WIDTH)
                .setDefaultValue(3840)
                .setMin(1)
                .setMax(65535)
                .setSaveConsumer(i -> Config.CAPTURE_WIDTH = i)
                .build());

        category.addEntry(entryBuilder.startIntField(new TranslatableText("fabrishot.config.height"), Config.CAPTURE_HEIGHT)
                .setDefaultValue(2160)
                .setMin(1)
                .setMax(65535)
                .setSaveConsumer(i -> Config.CAPTURE_HEIGHT = i)
                .build());
        return builder.build();
    }
}
