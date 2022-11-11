package com.geekbrains;

public class FileRequest extends AbstractMsg {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }
}
