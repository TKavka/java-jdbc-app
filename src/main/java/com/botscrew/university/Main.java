package com.botscrew.university;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/botscrew_db?useSSL=false";
    private static final String USERNAME = "botscrew";
    private static final String PASSWORD = "tiric2001";
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

    public static ResultSet getDataByDepartmentName(Connection connection, PreparedStatement statement, String departmentName) throws SQLException {

        statement.setString(1, departmentName);
        ResultSet resultSet = statement.executeQuery();

        return resultSet;
    }

    public static ResultSet globalDataSearch(Connection connection, PreparedStatement statement, String searchTemplate) throws SQLException {
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

        String departmentName = "";
        boolean flag = true;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            do {
                System.out.println("\n\n1.Show who is head of department");
                System.out.println("2.Show department statistics");
                System.out.println("3.Show the average salary for the department");
                System.out.println("4.Show count of employee for department");
                System.out.println("5.Global search");
                System.out.println("6.Exit\n");

                int choice = 0;
                try {
                    System.out.print("Make your choice: ");
                    choice = in.nextInt();
                    in.nextLine();
                    if (choice <= 0 || choice > 6) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    System.out.println("Invalid value type or value is <= 0 or > 6");
                    flag = false;
                }


                switch (choice) {
                    case 1: {
                        System.out.print("\nEnter department name: ");
                        departmentName = in.nextLine();

                        try (PreparedStatement statement = connection.prepareStatement(firstCaseQuery);
                             ResultSet resultSet = getDataByDepartmentName(connection, statement, departmentName)) {

                            if (!resultSet.next()) {
                                System.out.println("Department not exists");
                            } else {
                                do {
                                    String name = resultSet.getString(1);
                                    System.out.println("Head of " + departmentName + " department is " + resultSet.getString(1));
                                } while (resultSet.next());
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2: {
                        System.out.print("\nEnter department name: ");
                        departmentName = in.nextLine();

                        try (PreparedStatement statement = connection.prepareStatement(secondCaseQuery);
                             ResultSet resultSet = getDataByDepartmentName(connection, statement, departmentName)) {
                            if (!resultSet.next()) {
                                System.out.println("Department not exists");
                            } else {
                                System.out.println(departmentName + " statistic:");
                                do {
                                    String name = resultSet.getString(1);
                                    Integer totalCount = resultSet.getInt(2);
                                    System.out.println(name + " : " + totalCount);
                                } while (resultSet.next());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;
                    }

                    case 3: {
                        DecimalFormat df = new DecimalFormat("###.##");
                        System.out.print("\nEnter department name: ");
                        departmentName = in.nextLine();

                        try (PreparedStatement statement = connection.prepareStatement(thirdCaseQuery);
                             ResultSet resultSet = getDataByDepartmentName(connection, statement, departmentName)) {
                            while (resultSet.next()) {
                                double salary = resultSet.getDouble(1);
                                System.out.println(salary != 0 ?
                                        "Avg salary: " + df.format(salary) : "Department not exists");

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                    case 4: {
                        System.out.print("\nEnter department name: ");
                        departmentName = in.nextLine();

                        try (PreparedStatement statement = connection.prepareStatement(forthCaseQuery);
                             ResultSet resultSet = getDataByDepartmentName(connection, statement, departmentName)) {
                            while (resultSet.next()) {
                                int totalCount = resultSet.getInt(1);
                                System.out.println(totalCount != 0 ?
                                        "Count of employees: " + totalCount : "Department not exists");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 5: {
                        System.out.print("\nEnter search template: ");
                        String searchTemplate = in.nextLine();

                        try (PreparedStatement statement = connection.prepareStatement(fifthCaseQuery);
                             ResultSet resultSet = globalDataSearch(connection, statement, searchTemplate)) {

                            if (!resultSet.next()) {
                                System.out.println("Not found any value by template");
                            } else {
                                System.out.println("\nSearch results:");
                                do {
                                    String searchResults = resultSet.getString(1) + " " + resultSet.getString(2);
                                    System.out.println(searchResults);
                                } while (resultSet.next());
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
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
