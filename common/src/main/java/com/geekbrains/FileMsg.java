package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
@Data
@EqualsAndHashCode(callSuper = true)
public class FileMsg extends AbstractMsg {

    private String fileName;
    private byte[] data;

    public FileMsg(Path path) throws IOException {
        fileName = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }
}
