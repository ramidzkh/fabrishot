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

import com.mojang.blaze3d.platform.Window;
import me.ramidzkh.fabrishot.config.Config;
import me.ramidzkh.fabrishot.event.FramebufferCaptureCallback;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import java.io.IOException;
import java.nio.file.Path;

public class CaptureTask {
    private final Minecraft minecraft;
    private final Path file;
    private boolean hudHidden;
    private int frame;

    private int prevWidth, prevHeight;

    public CaptureTask(Path file) {
        this(Minecraft.getInstance(), file);
    }

    public CaptureTask(Minecraft minecraft, Path file) {
        this.minecraft = minecraft;
        this.file = file;
    }

    public void restoreResolution() {
        Window window = minecraft.getWindow();

        window.setWidth(prevWidth);
        window.setHeight(prevHeight);
    }

    public void setResolution(int width, int height) {
        Window window = minecraft.getWindow();

        prevWidth = window.getWidth();
        prevHeight = window.getHeight();

        window.setWidth(width);
        window.setHeight(height);
    }

    public boolean onRenderTick() {
        if (frame == 0) {
            hudHidden = minecraft.gui.hud.isHidden();
            if (!hudHidden && Config.HIDE_HUD) {
                minecraft.gui.hud.toggle();
            }

            frame++;
            return false;
        } else if (frame < Config.CAPTURE_DELAY) {
            frame++;
            return false;
        } else {
            Screenshot.takeScreenshot(minecraft.gameRenderer.mainRenderTarget(), image -> {
                Util.ioPool().execute(() -> {
                    try (image) {
                        FramebufferCaptureCallback.EVENT.invoker().onCapture(image);

                        if (Config.SAVE_FILE) {
                            FramebufferWriter.write(image, file);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });

            if (!hudHidden && Config.HIDE_HUD) {
                minecraft.gui.hud.toggle();
            }
            return true;
        }
    }
}
