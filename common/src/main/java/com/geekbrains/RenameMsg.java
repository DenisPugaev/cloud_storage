package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RenameMsg extends AbstractMsg {

    private String oldFileName;

    private String newFileName;

    private String message;



    public RenameMsg(String oldFileName, String newFileName) {
        this.oldFileName = oldFileName;
        this.newFileName = newFileName;
    }
    public RenameMsg(String message) {
        this.message = message;
    }
}

