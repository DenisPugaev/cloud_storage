package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateFileListMsg  extends  AbstractMsg{
    private ArrayList<String> serverFileList;

    public UpdateFileListMsg(ArrayList<String> serverFileList) {
        this.serverFileList = serverFileList;
    }

    public UpdateFileListMsg() {
    }

}

