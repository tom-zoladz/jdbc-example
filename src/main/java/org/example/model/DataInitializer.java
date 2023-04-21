package org.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataInitializer {

    private Connection connection;

    public DataInitializer(Connection connection) {
        this.connection = connection;
    }

    public void initData() throws SQLException {

        // To make below "insert" work we need to drop the table and create it again
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS worker"); // Najpierw usuwamy workera, bo ma on foreign key w relacji do departmentu, więc departmentu nie będzie można usunąć, jeśli w workerze będą jakieś dane
        statement.execute("DROP TABLE IF EXISTS department");

        String createDepartmentQuery = """
                    CREATE TABLE IF NOT EXISTS department (
                        department_id INT PRIMARY KEY,
                        department_name VARCHAR(50)
                    );
                    """;
        String createWorkerQuery = """
                    CREATE TABLE IF NOT EXISTS worker (
                        worker_id INT PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        hire_date DATE,
                        department_id int,
                        FOREIGN KEY (department_id) REFERENCES department(department_id)
                    );
                    """;
        statement.execute(createDepartmentQuery);
        statement.execute(createWorkerQuery);
    }
}
