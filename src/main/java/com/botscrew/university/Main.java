package com.botscrew.university;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/botscrew_db?useSSL=false";
    private static final String USERNAME = "add_your_username";
    private static final String PASSWORD = "add_your_password";
    private static final String firstCaseQuery = "SELECT department.head FROM department WHERE department.name=?";
    private static final String secondCaseQuery =
            "SELECT degree_name, count(degree_name) " +
            "FROM lector " +
            "LEFT JOIN lector_department AS ld " +
            "ON lector.id = ld.lector_id " +
            "LEFT JOIN department " +
            "ON ld.department_id = department.id " +
            "WHERE department.name = ? " +
            "GROUP BY degree_name";
    private static final String thirdCaseQuery = "" +
            "SELECT AVG(salary) " +
            "FROM lector " +
            "LEFT JOIN lector_department AS ld " +
            "ON lector.id = ld.lector_id " +
            "LEFT JOIN department " +
            "ON ld.department_id = department.id " +
            "WHERE department.name = ?";
    private static final String forthCaseQuery = "" +
            "SELECT COUNT(*) " +
            "FROM lector " +
            "LEFT JOIN lector_department AS ld " +
            "ON lector.id = ld.lector_id " +
            "LEFT JOIN department " +
            "ON ld.department_id = department.id " +
            "WHERE department.name = ?";
    private static final String fifthCaseQuery = "" +
            "SELECT first_name, last_name " +
            "FROM lector " +
            "LEFT JOIN lector_department AS ld " +
            "ON lector.id = ld.lector_id " +
            "LEFT JOIN department " +
            "ON ld.department_id = department.id " +
            "WHERE first_name RLIKE ? " +
            "OR last_name RLIKE ? " +
            "GROUP BY lector.id";

    public static ResultSet getDataByDepartmentName(Connection connection, PreparedStatement statement, String query, String departmentName) throws SQLException {
        statement = connection.prepareStatement(query);
        statement.setString(1, departmentName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    public static ResultSet globalDataSearch(Connection connection, PreparedStatement statement, String query, String searchTemplate) throws SQLException {
        statement = connection.prepareStatement(query);

        statement.setString(1, searchTemplate);
        statement.setString(2, searchTemplate);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            PreparedStatement statement = null;
            String departmentName = "";

            boolean flag = true;

            do {
                System.out.println("\n\n1.Show who is head of department");
                System.out.println("2.Show department statistics");
                System.out.println("3.Show the average salary for the department");
                System.out.println("4.Show count of employee for department");
                System.out.println("5.Global search");
                System.out.println("6.Exit\n");

                System.out.print("Make your choice: ");
                int choice = in.nextInt();
                in.nextLine();



                switch (choice) {
                    case 1: {
                        System.out.print("Enter department name: ");
                        departmentName = in.nextLine();

                        ResultSet resultSet = getDataByDepartmentName(connection, statement, firstCaseQuery, departmentName);;

                        while (resultSet.next()) {
                            String name = resultSet.getString(1);
                            System.out.println("Head of " + departmentName + " department is " + resultSet.getString(1));
                        }
                        break;
                    }
                    case 2: {
                        System.out.print("Enter department name: ");
                        departmentName = in.nextLine();
                        System.out.println("\n" + departmentName + " statistic:");

                        ResultSet resultSet = getDataByDepartmentName(connection, statement, secondCaseQuery, departmentName);

                        while (resultSet.next()) {
                            String name = resultSet.getString(1);
                            Integer totalCount = resultSet.getInt(2);
                            System.out.println(name + " : " + totalCount);
                        }
                        break;
                    }

                    case 3: {
                        DecimalFormat df = new DecimalFormat("###.##");
                        System.out.print("Enter department name: ");
                        departmentName = in.nextLine();
                        ResultSet resultSet = getDataByDepartmentName(connection, statement, thirdCaseQuery, departmentName);
                        while (resultSet.next()) {

                            System.out.println("Avg salary: " + df.format(resultSet.getDouble(1)));
                        }
                        break;
                    }
                    case 4: {
                        System.out.print("Enter department name: ");
                        departmentName = in.nextLine();

                        ResultSet resultSet = getDataByDepartmentName(connection, statement, forthCaseQuery, departmentName);

                        while (resultSet.next()) {
                            Integer totalCount = resultSet.getInt(1);
                            System.out.println("Count of employees: " + totalCount);
                        }
                        break;
                    }
                    case 5: {
                        System.out.print("Enter search template: ");
                        String searchTemplate = in.nextLine();

                        ResultSet resultSet = globalDataSearch(connection, statement, fifthCaseQuery, searchTemplate);

                        System.out.println("Search results:");
                        while (resultSet.next()) {
                            String searchResults = resultSet.getString(1) + " " + resultSet.getString(2);
                            System.out.println(searchResults);
                        }
                        break;
                    }
                    case 6: {
                        flag = false;
                        break;
                    }
                }
            } while (flag);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
