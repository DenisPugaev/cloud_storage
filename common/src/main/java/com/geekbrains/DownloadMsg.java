package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DownloadMsg extends  AbstractMsg{

    private String fileName;

    public DownloadMsg(String fileName) {
        this.fileName = fileName;
    }

}
