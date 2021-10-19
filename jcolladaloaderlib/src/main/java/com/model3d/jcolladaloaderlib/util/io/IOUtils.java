/*
MIT License

Copyright (c) 2020 The 3Deers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.model3d.jcolladaloaderlib.util.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public final class IOUtils {

    public static byte[] read(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(file);
        byte[] data = read(fis);
        fis.close();
        return data;
    }

    /**
     * Read fully the input stream and return the bytes.
     *
     * @param is input stream
     * @return the bytes
     * @throws IOException if there is an error reading from the stream
     */
    public static byte[] read(InputStream is) throws IOException {
        byte[] isData = new byte[512];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        while ((nRead = is.read(isData, 0, isData.length)) != -1) {
            buffer.write(isData, 0, nRead);
        }
        return buffer.toByteArray();
    }

    public static FloatBuffer createFloatBuffer(int floats) {
        return createNativeByteBuffer(floats * 4).asFloatBuffer();
    }

    public static FloatBuffer createFloatBuffer(List<float[]> vectorArray, int stride) {
        final FloatBuffer floatBuffer = createFloatBuffer(vectorArray.size() * stride);
        for (int i = 0; i < vectorArray.size(); i++) {
            floatBuffer.put(vectorArray.get(i));
        }
        return floatBuffer;
    }

    public static IntBuffer createIntBuffer(int integers) {
        return createNativeByteBuffer(integers * 4).asIntBuffer();
    }

    public static ShortBuffer createShortBuffer(int shorts) {
        return createNativeByteBuffer(shorts * 2).asShortBuffer();
    }

    public static ByteBuffer createNativeByteBuffer(int length) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(length);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }


}
