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
import me.ramidzkh.fabrishot.event.FramebufferCaptureCallback;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class FramebufferCapturer {

    private static final int COMPONENT_COUNT = 3; // RGB
    private static final int BYTES_PER_PIXEL = COMPONENT_COUNT; // 8 bits per component

    private final ByteBuffer bb;
    private final Dimension dim;

    public FramebufferCapturer() {
        dim = getCurrentDimension();
        bb = ByteBuffer.allocateDirect(dim.width() * dim.height() * BYTES_PER_PIXEL);
    }

    public int getBytesPerPixel() {
        return BYTES_PER_PIXEL;
    }

    public int getChannelCount() {
        return COMPONENT_COUNT;
    }

    public ByteBuffer getDataBuffer() {
        return bb;
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
            throw new IllegalStateException(String.format("Display size changed! %s != %s", dim1, dim2));
        }

        // set alignment flags
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        MinecraftInterface.writeFramebuffer(bb, BYTES_PER_PIXEL);

        bb.rewind();
        FramebufferCaptureCallback.EVENT.invoker().onCapture(dim2, bb);
    }
}
