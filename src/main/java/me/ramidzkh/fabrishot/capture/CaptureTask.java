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

package me.ramidzkh.fabrishot.capture;

import me.ramidzkh.fabrishot.MinecraftInterface;
import me.ramidzkh.fabrishot.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Util;

import java.io.IOException;
import java.nio.file.Path;

public class CaptureTask {

    private final Path file;

    private int frame;
    private int displayWidth;
    private int displayHeight;
    private boolean hudHidden;

    public CaptureTask(Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    public float getScaleFactor() {
        if (MinecraftClient.getInstance().options.getGuiScale().getValue() == 0) {
            return 1;
        }

        // might want to revisit this for weird screenshot resolutions
        return Math.min((float) MinecraftInterface.getDisplayWidth() / displayWidth, (float) MinecraftInterface.getDisplayHeight() / displayHeight);
    }

    public boolean onRenderTick() {
        // override viewport size (the following frame will be black)
        if (frame == 0) {
            displayWidth = MinecraftInterface.getDisplayWidth();
            displayHeight = MinecraftInterface.getDisplayHeight();
            hudHidden = MinecraftClient.getInstance().options.hudHidden;

            int width = Config.CAPTURE_WIDTH;
            int height = Config.CAPTURE_HEIGHT;

            // resize viewport/framebuffer
            MinecraftInterface.resize(width, height);
            MinecraftClient.getInstance().options.hudHidden |= Config.HIDE_HUD;
        } else if (frame >= Config.CAPTURE_DELAY) {
            // capture screenshot and restore viewport size
            try {
                FramebufferCapturer fbc = new FramebufferCapturer();
                fbc.capture();

                if (Config.SAVE_FILE) {
                    Util.getIoWorkerExecutor().execute(() -> {
                        FramebufferWriter fbw = new FramebufferWriter(file, fbc);

                        try {
                            fbw.write();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
                }
            } finally {
                // restore viewport/framebuffer
                MinecraftInterface.resize(displayWidth, displayHeight);
                MinecraftClient.getInstance().options.hudHidden = hudHidden;
            }
        }

        frame++;
        return frame > Config.CAPTURE_DELAY;
    }
}
