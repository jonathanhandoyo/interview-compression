package com.jonathan.compression;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class DecompressorTest {
    private String outputPath = "kafka_2.12-0.10.2.0";
    private String inputPath = "kafka_2.12-0.10.2.0.zip";

    @BeforeEach
    @AfterEach
    void cleanup() {
        try {
            Files.walk(Paths.get(outputPath))
                    .map(Path::toFile)
                    .sorted((o1, o2) -> -o1.compareTo(o2))
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDecompress() {
        Decompressor decompressor = new Decompressor();
        decompressor.decompress(inputPath, outputPath);

        Path result = Paths.get(outputPath);

        assertTrue(result.toFile().exists());
        assertTrue(result.toFile().isDirectory());
    }

}