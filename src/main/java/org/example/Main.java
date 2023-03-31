package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        // Creating jdbc connection to MySql db, providing with user and password
        // Connection and DriverManager are part of java.sql package
        // In order to make it work we need to add mysql-connector-java dependency in the pom file

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "sqlkurs") ) {

            // DB operations

            // To make below "insert" work we need to drop the table and create it again
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE department");

            String createDepartmentQuery = """
                    CREATE TABLE IF NOT EXISTS department (
                        department_id INT PRIMARY KEY,
                        department_name VARCHAR(50)
                    );
                    """;
            statement.execute(createDepartmentQuery);


            // In order to parametrize a query, we need to use PreparedStatement
            String insertDepartmentQuery = """
                    INSERT INTO department (department_id, department_name)
                    VALUES(?, ?);
                    """;
            PreparedStatement insertDepartment = connection.prepareStatement(insertDepartmentQuery);
            insertDepartment.setInt(1,1); // indexing from 1 unfortunately
            insertDepartment.setString(2, "Ministry of Magic");
            insertDepartment.executeUpdate();

            // Read the table content
            String getAllDepartmentsQuery = "SELECT * FROM department";
            Statement getAllDepartmentsStatement = connection.createStatement();

            ResultSet resultSet = getAllDepartmentsStatement.executeQuery(getAllDepartmentsQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                System.out.println("Department " + name + " with id " + id);
            }



            // Closing connection
            // connection.close(); // redundant after putting connection creating into the try brackets

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}