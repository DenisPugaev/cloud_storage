package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileRequest extends AbstractMsg {
    private String fileName;

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }

}
