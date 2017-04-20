package com.jonathan.compression;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        new Compressor().compress("C:/workspaces/kodeflip/poorly", "C:/workspaces/kodeflip/", 10);
//        new Decompressor().decompress("C:/workspaces/kodeflip/result.zip", "C:/workspaces/kodeflip/result");
    }
}
