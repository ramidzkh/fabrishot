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

import me.ramidzkh.fabrishot.config.Config;
import me.ramidzkh.fabrishot.event.FramebufferCaptureCallback;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

import java.io.IOException;
import java.nio.file.Path;

public class CaptureTask {

    private final Path file;
    private boolean hudHidden;
    private int frame;

    public CaptureTask(Path file) {
        this.file = file;
    }

    public boolean onRenderTick() {
        if (frame == 0) {
            hudHidden = Minecraft.getInstance().options.hideGui;
            Minecraft.getInstance().options.hideGui |= Config.HIDE_HUD;
            frame++;
            return false;
        } else if (frame < Config.CAPTURE_DELAY) {
            frame++;
            return false;
        } else {
            Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget(), image -> {
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

            Minecraft.getInstance().options.hideGui = hudHidden;
            return true;
        }
    }
}
