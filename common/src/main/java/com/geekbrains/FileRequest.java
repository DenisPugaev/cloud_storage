package com.geekbrains;

import lombok.Data;

@Data
public class FileRequest extends AbstractMsg {
    private String fileName;

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }

}
