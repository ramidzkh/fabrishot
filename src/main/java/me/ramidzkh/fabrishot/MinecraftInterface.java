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

import me.ramidzkh.fabrishot.mixins.WindowAccessor;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public interface MinecraftInterface {

    MinecraftClient CLIENT = MinecraftClient.getInstance();

    static void resize(int width, int height) {
        WindowAccessor accessor = (WindowAccessor) (Object) CLIENT.getWindow();

        accessor.setWidth(width);
        accessor.setHeight(height);
        accessor.setFramebufferWidth(width);
        accessor.setFramebufferHeight(height);

        CLIENT.onResolutionChanged();
    }

    static int getDisplayWidth() {
        return CLIENT.getWindow().getFramebufferWidth();
    }

    static int getDisplayHeight() {
        return CLIENT.getWindow().getFramebufferHeight();
    }

    static void writeFramebuffer(ByteBuffer pb, int bpp) {
        GL11.glReadPixels(0, 0, getDisplayWidth(), getDisplayHeight(), GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pb);

        byte[] line1 = new byte[getDisplayWidth() * bpp];
        byte[] line2 = new byte[getDisplayWidth() * bpp];

        // flip buffer vertically
        for (int i = 0; i < getDisplayHeight() / 2; i++) {
            int ofs1 = i * getDisplayWidth() * bpp;
            int ofs2 = (getDisplayHeight() - i - 1) * getDisplayWidth() * bpp;

            // read lines
            pb.position(ofs1);
            pb.get(line1);
            pb.position(ofs2);
            pb.get(line2);

            // write lines at swapped positions
            pb.position(ofs2);
            pb.put(line1);
            pb.position(ofs1);
            pb.put(line2);
        }
    }
}
