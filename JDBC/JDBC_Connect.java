package com.green.bank.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Connect {
    private Connection connection = null;

    public Connection getConnection() throws SQLException {
        try {
            // MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //  Change database name, username, password
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/banking_system?useSSL=false&serverTimezone=UTC",
                    "root",          // your MySQL username
                    "T@nush3805"       // your MySQL password
            );

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }

        return connection;
    }
}