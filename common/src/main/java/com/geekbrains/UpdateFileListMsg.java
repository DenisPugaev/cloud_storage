package com.geekbrains;

import java.util.ArrayList;

public class UpdateFileListMsg  extends  AbstractMsg{
    private ArrayList<String> serverFileList;

    public UpdateFileListMsg(ArrayList<String> serverFileList) {
        this.serverFileList = serverFileList;
    }

    public UpdateFileListMsg() {
    }

    public ArrayList<String> getServerFileList() {
        return serverFileList;
    }
}

