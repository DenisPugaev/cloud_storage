package com.geekbrains;

public class AuthMsg extends AbstractMsg {
    public String login;
    public String password;
    public String message;

    public AuthMsg() {
    }

    public AuthMsg(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AuthMsg(String message) { this.message = message;
    }

}
