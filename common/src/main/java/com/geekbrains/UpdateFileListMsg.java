package com.geekbrains;

import lombok.Data;

import java.util.ArrayList;
@Data
public class UpdateFileListMsg  extends  AbstractMsg{
    private ArrayList<String> serverFileList;

    public UpdateFileListMsg(ArrayList<String> serverFileList) {
        this.serverFileList = serverFileList;
    }

    public UpdateFileListMsg() {
    }

}

