package com.geekbrains;

public class DeleteMsg extends AbstractMsg {
    private String fileName;

    public DeleteMsg(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
