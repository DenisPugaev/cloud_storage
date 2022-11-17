package com.geekbrains;

import lombok.Data;

@Data
public class DownloadMsg extends  AbstractMsg{

    private String fileName;

    public DownloadMsg(String fileName) {
        this.fileName = fileName;
    }

}
