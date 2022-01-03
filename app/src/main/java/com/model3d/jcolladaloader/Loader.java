package com.model3d.jcolladaloader;

import android.content.Context;

import com.model3d.jcolladaloaderlib.ColladaLoader;
import com.model3d.jcolladaloaderlib.model.Object3DData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class Loader {

    protected static void load(Context context, String zipFilename, String outDir, String daeDir, String daeFilename){
        try {
            unzip(new File(zipFilename), new File(outDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ColladaLoader colladaLoader = new ColladaLoader();
        List<Object3DData> data = colladaLoader.loadFromExternalStorage(context, daeDir, daeFilename);
    }

    private static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }
}
