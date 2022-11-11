package com.geekbrains;

public class DownloadMsg extends  AbstractMsg{

    String fileName;

    public DownloadMsg(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
