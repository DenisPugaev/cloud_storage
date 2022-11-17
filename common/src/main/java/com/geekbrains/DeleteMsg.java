package com.geekbrains;

import lombok.Data;

@Data
public class DeleteMsg extends AbstractMsg {
    private String fileName;

    public DeleteMsg(String fileName) {
        this.fileName = fileName;
    }

}
