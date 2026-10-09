
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 100;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                // Wait for database to start
                Thread.sleep(3000);

                // Connect to database
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

        if (con == null) {
            System.out.println("Could not connect to database");
            System.exit(1);
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (SQLException e) {
                System.out.println(
                        "Error closing connection"
                );
            }
        }
    }

    /**
     * Get employee information by ID.
     */
    public Employee getEmployee(int ID) {
        if (con == null) {
            System.out.println("Database not connected");
            return null;
        }

        try {
            // Create SQL statement
            String strSelect =
                    "SELECT emp_no, first_name, last_name " +
                            "FROM employees " +
                            "WHERE emp_no = ?";

            // Execute SQL statement
            try (PreparedStatement stmt =
                         con.prepareStatement(strSelect)) {

                stmt.setInt(1, ID);

                try (ResultSet rset = stmt.executeQuery()) {

                    // Check if employee exists
                    if (rset.next()) {
                        Employee emp = new Employee();

                        emp.emp_no = rset.getInt("emp_no");
                        emp.first_name = rset.getString("first_name");
                        emp.last_name = rset.getString("last_name");

                        return emp;
                    }
                }
            }

            return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Display employee information.
     */
    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " " +
                            emp.first_name + " " +
                            emp.last_name + "\n" +
                            emp.title + "\n" +
                            "Salary: " + emp.salary + "\n" +
                            emp.dept_name + "\n" +
                            "Manager: " + emp.manager + "\n"
            );
        }
    }

    /**
     * Main method.
     */
    public static void main(String[] args) {

        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Get Employee
        Employee emp = a.getEmployee(255530);

        // Display results
        a.displayEmployee(emp);

        // Disconnect from database
        a.disconnect();
    }
}
