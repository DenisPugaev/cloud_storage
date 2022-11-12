package com.geekbrains;


import java.sql.*;

import static com.geekbrains.Server.log;

public class SqlAuthService {

    private static Connection connection;
    private static Statement stmt;


    static void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            stmt = connection.createStatement();
            log.info("DB_Start");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            log.error("DB_Error");
        }
    }

    static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String authUser(String login, String password) {

        connection();
        String nickNameDB = null;
        String passwordDB = null;
        log.info("Данные которые пришли на авторизацию в БД= "+ login+ " | "+ password);
        try {
            ResultSet rs = stmt.executeQuery(String.format("SELECT nickName from users "+"WHERE login = '%s'", login));



            nickNameDB = rs.getString("login");
            passwordDB = rs.getString("password");
            System.out.println("Значение получено из ДБ - " + nickNameDB);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB_Error_auth");
        }
        disconnect();
        log.info("return from DB nickName = " + nickNameDB);

        return ((passwordDB != null) && (passwordDB.equals(password))) ? nickNameDB : null;

    }

    static void registrationNewUser(String login, String password, String nickName) {
       log.info("Данные которые пришли на регистрацию в БД= "+ login+ " | "+ password+ " | "+ nickName);
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users (login, password, nickName) VALUES (?, ?, ?);");
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, nickName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB_Error_Reg");
        }

    }


}
