package com.geekbrains;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationMsg extends AbstractMsg {
    private String login;
    private String password;
    private String nickName;
    private String message;

    public RegistrationMsg(String login, String password, String nickName) {
        this.login = login;
        this.password = password;
        this.nickName = nickName;
    }

    public RegistrationMsg(String message) { this.message = message;}


}


