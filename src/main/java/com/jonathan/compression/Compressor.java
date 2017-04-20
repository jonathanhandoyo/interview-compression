package com.jonathan.compression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {
    public void compress(String inputPath, String outputPath) {
        try (FileOutputStream fos = new FileOutputStream(outputPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            Files.walk(Paths.get(this.getClass().getClassLoader().getResource(inputPath).toURI()))
                    .filter(path -> !Files.isDirectory(path))
                    .map(path -> {
                        try {
                            File file = path.toFile();
                            System.out.println("Writing >> [" + Files.size(path) + "] >> " + file.getAbsolutePath());
                            return file;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(file -> {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            String path = file.getCanonicalPath().substring(inputPath.length() + 1, file.getCanonicalPath().length());
                            zos.putNextEntry(new ZipEntry(path));

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, length);
                            }

                            zos.closeEntry();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compress(String inputPath, String outputPath, Integer sizeThreshold) {
        final Integer[] index = {0};
        final Long[] currentSize = {0L};

        try {
            final FileOutputStream[] fos = {new FileOutputStream(outputPath + "." + index[0])};
            final ZipOutputStream[] zos = {new ZipOutputStream(fos[0])};

            Files.walk(Paths.get(this.getClass().getClassLoader().getResource(inputPath).toURI()))
                    .filter(path -> !Files.isDirectory(path))
                    .map(path -> {
                        try {
                            File file = path.toFile();
                            System.out.println("Writing >> [curr: " + Files.size(path) + " / accu: " + currentSize[0] + "] >> " + file.getAbsolutePath());
                            return file;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(file -> {
                        try (FileInputStream fis = new FileInputStream(file)) {
                            String path = file.getCanonicalPath().substring(inputPath.length() + 1, file.getCanonicalPath().length());
                            ZipEntry entry = new ZipEntry(path);

                            if (currentSize[0] + entry.getCompressedSize() > sizeThreshold * 1024 * 1024) {
                                zos[0].close();
                                fos[0].close();

                                currentSize[0] = 0L;
                                index[0]++;

                                fos[0] = new FileOutputStream(outputPath + "." + index[0]);
                                zos[0] = new ZipOutputStream(fos[0]);
                            }

                            zos[0].putNextEntry(entry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos[0].write(buffer, 0, length);
                            }

                            zos[0].closeEntry();
                            currentSize[0] += entry.getCompressedSize();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
            zos[0].close();
            fos[0].close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
