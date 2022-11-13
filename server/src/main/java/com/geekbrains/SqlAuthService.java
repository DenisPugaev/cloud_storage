package com.geekbrains;


import java.sql.*;

import static com.geekbrains.Server.log;

public class SqlAuthService {

    public SqlAuthService() {
        dbInit();
    }

    static String authUser(String login, String password) {


        String nickNameDB = null;
        String passwordDB = null;
        String query = String.format("SELECT nickName, password from users " + "WHERE login = '%s'", login);
        log.info("Данные которые пришли на авторизацию в БД= " + login + " | " + password);
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:users.db");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            rs.next();
            nickNameDB = rs.getString("nickName");
            passwordDB = rs.getString("password");
            System.out.println("Значение получено из ДБ - " + nickNameDB);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB_Error_auth");
        }
        log.info("return from DB nickName = " + nickNameDB);

        return ((passwordDB != null) && (passwordDB.equals(password))) ? nickNameDB : null;

    }

    static void registrationNewUser(String login, String password, String nickName) {

        log.info("Данные которые пришли на регистрацию в БД= " + login + " | " + password + " | " + nickName);

    String query = "INSERT INTO users (login, password, nickName) VALUES (?, ?, ?);";

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:users.db");
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, nickName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB_Error_Reg");
        }

    }

    private static void dbInit() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}



