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

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigBridge implements ConfigScreenFactory<Screen> {

    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Text.translatable("fabrishot.config.title"))
                .setSavingRunnable(Config::save)
                .setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("fabrishot.config.category"));

        category.addEntry(entryBuilder.startStrField(Text.translatable("fabrishot.config.custom_file_name"), Config.CUSTOM_FILE_NAME)
                .setTooltip(Text.translatable("fabrishot.config.custom_file_name.tooltip"))
                .setDefaultValue("huge_%time%")
                .setSaveConsumer(b -> Config.CUSTOM_FILE_NAME = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.override_screenshot_key"), Config.OVERRIDE_SCREENSHOT_KEY)
                .setDefaultValue(false)
                .setSaveConsumer(b -> Config.OVERRIDE_SCREENSHOT_KEY = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("fabrishot.config.save_file"), Config.SAVE_FILE)
                .setDefaultValue(true)
                .setSaveConsumer(b -> Config.SAVE_FILE = b)
                .build());

        category.addEntry(entryBuilder.startIntField(Text.translatable("fabrishot.config.width"), Config.CAPTURE_WIDTH)
                .setDefaultValue(3840)
                .setMin(1)
                .setMax(Math.min(65535, RenderSystem.maxSupportedTextureSize()))
                .setSaveConsumer(i -> Config.CAPTURE_WIDTH = i)
                .build());

        category.addEntry(entryBuilder.startIntField(Text.translatable("fabrishot.config.height"), Config.CAPTURE_HEIGHT)
                .setDefaultValue(2160)
                .setMin(1)
                .setMax(Math.min(65535, RenderSystem.maxSupportedTextureSize()))
                .setSaveConsumer(i -> Config.CAPTURE_HEIGHT = i)
                .build());

        category.addEntry(entryBuilder.startIntField(Text.translatable("fabrishot.config.delay"), Config.CAPTURE_DELAY)
                .setTooltip(Text.translatable("fabrishot.config.delay.tooltip"))
                .setDefaultValue(3)
                .setMin(3)
                .setSaveConsumer(i -> Config.CAPTURE_DELAY = i)
                .build());
        return builder.build();
    }
}
