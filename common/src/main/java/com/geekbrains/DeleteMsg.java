package com.geekbrains;


import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)

public class DeleteMsg extends AbstractMsg {
    private String fileName;

    public DeleteMsg(String fileName) {
        this.fileName = fileName;
    }

}
