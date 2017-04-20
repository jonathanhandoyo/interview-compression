package com.jonathan.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Decompressor {
    public void decompress(String inputFile, String outputPath) {
        Path path = Paths.get(outputPath);

        if (!path.toFile().exists()) path.toFile().mkdir();

        try (FileInputStream fis = new FileInputStream(inputFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {
                String entryName = entry.getName();
                File file = new File(outputPath + File.separator + entryName);
                System.out.println("Writing >> " + entryName + " >> " + file.getAbsolutePath());

                if (entry.isDirectory()) {
                    File dir = new File(file.getAbsolutePath());
                    if (!dir.exists()) dir.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    fos.close();
                }

                zis.closeEntry();
                entry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
