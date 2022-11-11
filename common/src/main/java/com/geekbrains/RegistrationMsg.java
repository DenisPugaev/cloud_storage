package com.geekbrains;

public class RegistrationMsg extends AbstractMsg {
    public String login;
    public String password;
    public String nickName;
    public String message;

    public RegistrationMsg(String login, String password, String nickName) {
        this.login = login;
        this.password = password;
        this.nickName = nickName;
    }

    public RegistrationMsg(String message) { this.message = message;}
}


