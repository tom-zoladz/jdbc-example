package org.example.dao;

import org.example.model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDao implements Repository<Department> {
    private final Connection connection;

    public DepartmentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Department save(Department department) throws SQLException {
        String saveQuery = "INSERT INTO DEPARTMENT (department_id, department_name) VALUES(?,?)";

        PreparedStatement statement = connection.prepareStatement(saveQuery);
        statement.setInt(1, department.getDepartmentId());
        statement.setString(2, department.getDepartmentName());

        int affectedRows = statement.executeUpdate(); // jeśli 0, to nie zmieniło się nic w db
        if (affectedRows == 0) {
            System.out.println("Unable to save Department " + department.getDepartmentName());
        }

        // Just to have the returned object, actually it should be got from db
        return department;
    }

    @Override
    public List<Department> getAll() throws SQLException {
        String getAllDepartmentsQuery = "SELECT * FROM department";
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(getAllDepartmentsQuery);

        List<Department> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Department(resultSet.getInt(1), resultSet.getString(2)));
        }

        return list;
    }

    @Override
    public Optional<Department> getById(int id) throws SQLException {
        String getByIdQuery = "SELECT * FROM department WHERE department_id=?";
        PreparedStatement statement = connection.prepareStatement(getByIdQuery);
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return Optional.of(new Department(resultSet.getInt(1), resultSet.getString(2)));
        }

        return Optional.empty();
    }

    @Override
    public Department update(Department department) throws SQLException {
        String updateQuery = "UPDATE department SET department_name=? WHERE department_id=?";
        PreparedStatement statement = connection.prepareStatement(updateQuery);
        statement.setString(1, department.getDepartmentName());
        statement.setInt(2, department.getDepartmentId());

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Unable to update Department " + department.getDepartmentName());
        }

        return department;
    }

    @Override
    public boolean removeById(int id) throws SQLException {
        String removeQuery = "DELETE FROM department WHERE department_id=?";
        PreparedStatement statement = connection.prepareStatement(removeQuery);
        statement.setInt(1, id);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("No rows affected");
            return false;
        }
        return true;
    }

}
