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

import me.ramidzkh.fabrishot.event.ScreenshotSaveCallback;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBImageWrite;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FramebufferWriter {

    protected final FramebufferCapturer fbc;
    protected final Path file;

    public FramebufferWriter(Path file, FramebufferCapturer fbc) {
        this.file = file;
        this.fbc = fbc;
    }

    public void write() throws IOException {
        try (FileChannel fc = FileChannel.open(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            writeImage(fc);
        }

        ScreenshotSaveCallback.EVENT.invoker().onSaved(file);
    }

    private void writeImage(FileChannel fc) throws IOException {
        Dimension dim = fbc.getCaptureDimension();

        try (WriteCallback callback = new WriteCallback(fc)) {
            STBImageWrite.stbi_write_png_to_func(callback, 0L, dim.width, dim.height, fbc.getChannelCount(), fbc.getDataBuffer(), 0);

            if (callback.exception != null) {
                throw callback.exception;
            }
        }
    }

    private static class WriteCallback extends STBIWriteCallback implements AutoCloseable, Closeable {
        private final WritableByteChannel channel;
        private IOException exception;

        private WriteCallback(WritableByteChannel channel) {
            this.channel = channel;
        }

        @Override
        public void invoke(long context, long data, int size) {
            if (this.exception != null) {
                return;
            }

            ByteBuffer buf = STBIWriteCallback.getData(data, size);

            try {
                this.channel.write(buf);
            } catch (IOException e) {
                this.exception = e;
            }
        }

        @Override
        public void close() {
            this.free();
        }
    }
}
