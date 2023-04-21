package org.example.dao;


import org.example.model.Worker;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkerDao implements Repository<Worker> {

    private final Connection connection;

    public WorkerDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Worker save(Worker worker) throws SQLException {
        String saveQuery = "INSERT INTO worker (worker_id, first_name, last_name, hire_date, department_id) VALUES(?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(saveQuery);
        statement.setInt(1, worker.getWorkerId());
        statement.setString(2, worker.getFirstName());
        statement.setString(3, worker.getLastName());
        statement.setDate(4, Date.valueOf(worker.getHireDate()));
        statement.setInt(5, worker.getDepartmentId());

        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("Unable to save worker " + worker.getFirstName() + " " + worker.getLastName());
        }
        return worker;
    }

    @Override
    public List<Worker> getAll() throws SQLException {
        String getAllQuery = "SELECT * FROM worker";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getAllQuery);

        List<Worker> workers = new ArrayList<>();
        while (resultSet.next()) {
            workers.add(new Worker(
               resultSet.getInt("worker_id"),
               resultSet.getString("first_name"),
               resultSet.getString("last_name"),
               LocalDate.parse(resultSet.getDate("hire_date").toString()),
               resultSet.getInt("department_id")
            ));
        }

        return workers;
    }

    @Override
    public Optional<Worker> getById(int id) throws SQLException {
        String getByIdQuery = "SELECT * FROM worker WHERE worker_id=?";
        PreparedStatement statement = connection.prepareStatement(getByIdQuery);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Worker worker = new Worker(
                    resultSet.getInt("worker_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    LocalDate.parse(resultSet.getDate("hire_date").toString()),
                    resultSet.getInt("department_id")
            );
            return Optional.of(worker);
        }

        return Optional.empty();
    }

    @Override
    public Worker update(Worker worker) throws SQLException {
        String updateQuery = "UPDATE worker SET first_name=?, last_name=?, hire_date=?, department_id=? WHERE worker_id=?";

        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, worker.getFirstName());
        statement.setString(2, worker.getLastName());
        statement.setDate(3, Date.valueOf(worker.getHireDate()));
        statement.setInt(4, worker.getDepartmentId());
        statement.setInt(5, worker.getWorkerId());

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Unable to update Worker " + worker.getFirstName());
        }

        return worker;
    }

    @Override
    public boolean removeById(int id) throws SQLException {
        String removeQuery = "DELETE FROM worker WHERE worker_id=?";
        PreparedStatement statement = connection.prepareStatement(removeQuery);
        statement.setInt(1, id);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Unable to remove worker of id " + id);
            return false;
        }

        return true;
    }
}
