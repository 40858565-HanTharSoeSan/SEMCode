
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        Connection con = null;
        int retries = 100;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                Thread.sleep(3000);

                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees" +
                                "?allowPublicKeyRetrieval=true" +
                                "&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;

            } catch (SQLException e) {
                System.out.println(
                        "Failed to connect attempt " + (i + 1)
                );
                System.out.println(e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted");
                break;
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(
                        "Error closing connection"
                );
            }
        } else {
            System.out.println("Could not connect to database");
            System.exit(1);
        }
    }
}
