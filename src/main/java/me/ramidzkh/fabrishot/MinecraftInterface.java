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

import me.ramidzkh.fabrishot.mixins.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;

public interface MinecraftInterface {

    MinecraftClient CLIENT = MinecraftClient.getInstance();

    static void resize(int width, int height) {
        CLIENT.getFramebuffer().resize(width, height, true);

        WindowAccessor accessor = (WindowAccessor) (Object) CLIENT.getWindow();

        accessor.setWidth(width);
        accessor.setHeight(height);
        accessor.setFramebufferWidth(width);
        accessor.setFramebufferHeight(height);

        CLIENT.onResolutionChanged();
    }

    static int getDisplayWidth() {
        return CLIENT.getWindow().getWidth();
    }

    static int getDisplayHeight() {
        return CLIENT.getWindow().getHeight();
    }

    static void writeFramebuffer(ByteBuffer pixelBuffer, boolean flipColors, boolean flipLines) {
        GL11.glReadPixels(0, 0, getDisplayWidth(), getDisplayHeight(), flipColors ? GL12.GL_BGR : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixelBuffer);

        if (!flipLines) {
            return;
        }

        final int BPP = 3; // bytes per pixel
        byte[] line1 = new byte[getDisplayWidth() * BPP];
        byte[] line2 = new byte[getDisplayWidth() * BPP];

        // flip buffer vertically
        for (int i = 0; i < getDisplayHeight() / 2; i++) {
            int ofs1 = i * getDisplayWidth() * BPP;
            int ofs2 = (getDisplayHeight() - i - 1) * getDisplayWidth() * BPP;

            // read lines
            pixelBuffer.position(ofs1);
            pixelBuffer.get(line1);
            pixelBuffer.position(ofs2);
            pixelBuffer.get(line2);

            // write lines at swapped positions
            pixelBuffer.position(ofs2);
            pixelBuffer.put(line1);
            pixelBuffer.position(ofs1);
            pixelBuffer.put(line2);
        }
    }
}
