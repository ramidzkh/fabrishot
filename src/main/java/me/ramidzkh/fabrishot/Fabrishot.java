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

package me.ramidzkh.fabrishot;

import me.ramidzkh.fabrishot.capture.CaptureTask;
import me.ramidzkh.fabrishot.config.Config;
import me.ramidzkh.fabrishot.event.ScreenshotSaveCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Fabrishot {

    public static final KeyBinding SCREENSHOT_BINDING = new KeyBinding(
            "key.fabrishot.screenshot",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F9,
            "key.categories.misc");

    private static CaptureTask task;

    private static void printFileLink(Path path) {
        Text text = Text.literal(path.toFile().getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent.OpenFile(path)));
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("screenshot.success", text)));
    }

    public static void initialize() {
        KeyBindingHelper.registerKeyBinding(SCREENSHOT_BINDING);
        ScreenshotSaveCallback.EVENT.register(Fabrishot::printFileLink);
    }

    public static void startCapture() {
        if (task == null) {
            task = new CaptureTask(getScreenshotFile(MinecraftClient.getInstance()));
            MinecraftInterface.refresh();
        }
    }

    public static void onRenderPreOrPost() {
        if (task != null && task.onRenderTick()) {
            task = null;
            MinecraftInterface.refresh();
        }
    }

    private static Path getScreenshotFile(MinecraftClient client) {
        Path dir = client.runDirectory.toPath().resolve("screenshots");

        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        String world = null;

        if (client.getServer() != null) {
            world = client.getServer().getSaveProperties().getLevelName();
        } else if (client.getCurrentServerEntry() != null) {
            world = client.getCurrentServerEntry().name;
        }

        Path file;
        String prefix = Config.CUSTOM_FILE_NAME
                .replace("%time%", Util.getFormattedCurrentTime())
                .replace("%world%", world != null ? world : "no_world");

        // loop though suffixes while the file exists
        int i = 1;

        do {
            file = dir.resolve(prefix + (i++ == 1 ? "" : "_" + i) + Config.CAPTURE_FILE_FORMAT.extension());
        } while (Files.exists(file));

        return file;
    }

    public static boolean isInCapture() {
        return task != null;
    }
}
