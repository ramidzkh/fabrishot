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

package me.ramidzkh.fabrishot.capture;

import me.ramidzkh.fabrishot.MinecraftInterface;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class FramebufferCapturer {

    private static final int BPP = 3; // bytes per pixel

    private final ByteBuffer bb;
    private final Dimension dim;
    private boolean flipColors = false;
    private boolean flipLines = false;

    public FramebufferCapturer() {
        dim = getCurrentDimension();
        bb = ByteBuffer.allocateDirect(dim.width * dim.height * BPP);
    }

    public boolean isFlipColors() {
        return flipColors;
    }

    public void setFlipColors(boolean flipColors) {
        this.flipColors = flipColors;
    }

    public boolean isFlipLines() {
        return flipLines;
    }

    public void setFlipLines(boolean flipLines) {
        this.flipLines = flipLines;
    }

    public int getBytesPerPixel() {
        return BPP;
    }

    public ByteBuffer getByteBuffer() {
        bb.rewind();
        return bb.duplicate();
    }

    public Dimension getCaptureDimension() {
        return dim;
    }

    private Dimension getCurrentDimension() {
        return new Dimension(MinecraftInterface.getDisplayWidth(), MinecraftInterface.getDisplayHeight());
    }

    public void capture() {
        // check if the dimensions are still the same
        Dimension dim1 = getCurrentDimension();
        Dimension dim2 = getCaptureDimension();
        if (!dim1.equals(dim2)) {
            throw new IllegalStateException(String.format(
                    "Display size changed! %dx%d != %dx%d",
                    dim1.width, dim1.height,
                    dim2.width, dim2.height));
        }

        // set alignment flags
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        MinecraftInterface.writeFramebuffer(bb, flipColors, flipLines);
    }
}
