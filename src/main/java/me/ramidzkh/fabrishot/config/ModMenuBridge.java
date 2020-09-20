/*
 * MIT License
 *
 * Copyright (c) 2020 Ramid Khan
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
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.Properties;

public class ModMenuBridge implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        try (BufferedReader reader = Files.newBufferedReader(FabricLoader.getInstance().getConfigDir().resolve("fabrishot.properties"))) {
            Properties properties = new Properties();
            properties.load(reader);

            Config.CAPTURE_WIDTH = Integer.parseInt(properties.getProperty("width"));
            Config.CAPTURE_HEIGHT = Integer.parseInt(properties.getProperty("height"));
        } catch (Exception ignored) {
        }

        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            return new ClothConfigBridge();
        } else {
            return screen -> null;
        }
    }
}
