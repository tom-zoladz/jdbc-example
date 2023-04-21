package org.example.dao;

import org.example.model.Department;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T save(T t) throws SQLException;

    List<T> getAll() throws SQLException;
    Optional<T> getById(int id) throws SQLException;

    T update(T t) throws SQLException;

    boolean removeById(int id) throws SQLException;
}
