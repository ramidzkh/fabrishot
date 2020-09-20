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

package me.ramidzkh.fabrishot;

import me.ramidzkh.fabrishot.capture.CaptureTask;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fabrishot {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static CaptureTask task;

    public static void printFileLink(File file) {
        Text text = new LiteralText(file.getName()).formatted(Formatting.UNDERLINE).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())));
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new TranslatableText("screenshot.success", text)));
    }

    public static void initialize() {
        KeyBinding screenshot = new KeyBinding(
                "key.fabrishot.screenshot",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F9,
                "key.categories.misc");

        KeyBindingHelper.registerKeyBinding(screenshot);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (screenshot.wasPressed()) {
                task = new CaptureTask(getScreenshotFile(client));
            }
        });
    }

    public static void onRenderPreOrPost() {
        if (task != null) {
            try {
                if (task.onRenderTick()) {
                    Fabrishot.printFileLink(task.getFile().toFile());
                    task = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                task = null;
            }
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

        // loop though suffixes while the file exists
        int i = 0;
        Path file;

        do {
            file = dir.resolve(String.format("huge_%s_%04d.png", DATE_FORMAT.format(new Date()), i++));
        } while (Files.exists(file));

        return file;
    }
}
