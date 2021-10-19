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

package com.model3d.jcolladaloaderlib.util.android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ContentUtils {

    /**
     * Documents opened by the user. This list helps finding the relative filenames found in the model
     */
    private static Map<String, Uri> documentsProvided = new HashMap<>();

    private static ThreadLocal<Context> currentActivity = new ThreadLocal<>();

    private static File currentDir = null;

    private static Context getCurrentActivity() {
        return ContentUtils.currentActivity.get();
    }

    public static Uri getUri(String name) {
        return documentsProvided.get(name);
    }

    /**
     * Find the relative file that should be already selected by the user
     *
     * @param path relative file
     * @return InputStream of the file
     * @throws IOException if there is an error opening stream
     */
    public static InputStream getInputStream(String path) throws IOException {
        Uri uri = getUri(path);
        if (uri == null && currentDir != null) {
            uri = Uri.parse("file://" + new File(currentDir, path).getAbsolutePath());
        }
        if (uri != null) {
            return getInputStream(uri);
        }
        Log.w("ContentUtils", "Media not found: " + path);
        Log.w("ContentUtils", "Available media: " + documentsProvided);
        throw new FileNotFoundException("File not found: " + path);
    }

    public static InputStream getInputStream(Uri uri) throws IOException {
        Log.i("ContentUtils", "Opening stream ..." + uri);
        if (uri.getScheme().equals("android")) {
            if (uri.getPath().startsWith("/assets/")) {
                final String path = uri.getPath().substring("/assets/".length());
                Log.i("ContentUtils", "Opening asset: " + path);
                return getCurrentActivity().getAssets().open(path);
            } else if (uri.getPath().startsWith("/res/drawable/")){
                final String path = uri.getPath().substring("/res/drawable/".length()).replace(".png","");
                Log.i("ContentUtils", "Opening drawable: " + path);
                final int resourceId = getCurrentActivity().getResources()
                        .getIdentifier(path, "drawable", getCurrentActivity().getPackageName());
                return getCurrentActivity().getResources().openRawResource(resourceId);
            } else {
                throw new IllegalArgumentException("unknown android path: "+uri.getPath());
            }
        }
        if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
            return new URL(uri.toString()).openStream();
        }
        if (uri.getScheme().equals("content")) {
            return getCurrentActivity().getContentResolver().openInputStream(uri);
        }
        return getCurrentActivity().getContentResolver().openInputStream(uri);
    }
}