package com.geekbrains;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMsg extends AbstractMsg {

    private String fileName;
    private byte[] data;

    public String getFileName() {return fileName;}

    public byte[] getData() {
        return data;
    }

    public FileMsg(Path path) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);

    }
}
