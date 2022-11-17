package com.geekbrains;

import lombok.Data;

@Data
public class RenameMsg extends AbstractMsg {

    private String oldFileName;

    private String newFileName;

    public RenameMsg(String oldFileName, String newFileName) {
        this.oldFileName = oldFileName;
        this.newFileName = newFileName;
    }
}

