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
        String usernameDB = null;
        String passwordDB = null;
        try {
            ResultSet rs = stmt.executeQuery(String.format("SELECT nickname from users WHERE login = '%s'", login));

            if (rs.isClosed()) {
                return null;
            }

            usernameDB = rs.getString("username");
            passwordDB = rs.getString("password");
            System.out.println("Значение получено из ДБ - " + usernameDB);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("DB_Error_auth");
        }
        disconnect();
        log.info("return from DB nickName = " + usernameDB);

        return ((passwordDB != null) && (passwordDB.equals(password))) ? usernameDB : null;
    }

    static void registrationNewUser(String login, String password, String nickName) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO main (login, password, nickname) VALUES (?, ?, ?);");
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
