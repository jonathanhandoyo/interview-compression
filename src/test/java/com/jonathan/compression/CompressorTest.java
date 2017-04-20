package com.jonathan.compression;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CompressorTest {
    private String inputPath = "kafka_2.12-0.10.2.0";
    private String outputPath = "result.zip";

    @BeforeEach
    void beforeEach() {
        try {
            Files.list(Paths.get(""))
                    .filter(it -> it.toFile().getName().startsWith(outputPath))
                    .forEach(it -> it.toFile().delete());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCompressWithoutFileSizeThreshold() {
        Compressor compressor = new Compressor();
        compressor.compress(inputPath, outputPath);

        File result = Paths.get(outputPath).toFile();

        assertTrue(result.exists());
        assertTrue(result.isFile());
    }

    @Test
    void testCompressWithFileSizeThreshold() {
        Compressor compressor = new Compressor();
        compressor.compress(inputPath, outputPath, 10);

        try {
            Files.list(Paths.get(""))
                    .filter(it -> it.toFile().getName().startsWith(outputPath))
                    .forEach(it -> {
                        try {
                            assertTrue(10 * 1024 * 1024 > Files.size(it), it.toFile().getName() + " size exceeds threshold");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}