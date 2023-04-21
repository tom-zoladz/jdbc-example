package org.example;

import org.example.dao.DepartmentDao;
import org.example.dao.WorkerDao;
import org.example.model.DataInitializer;
import org.example.model.Department;
import org.example.model.Worker;
import org.example.service.WorkerFullInfoService;
import org.example.service.WorkerWithDepartment;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        // Creating jdbc connection to MySql db, providing with user and password
        // Connection and DriverManager are part of java.sql package
        // In order to make it work we need to add mysql-connector-java dependency in the pom file

        try (final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "root", "sqlkurs") ) {


            DataInitializer dataInitializer = new DataInitializer(connection);
            dataInitializer.initData();


            // CREATE (save)
            DepartmentDao departmentDao = new DepartmentDao(connection);
            departmentDao.save(new Department(1, "Ministry of Magic"));
            departmentDao.save(new Department(2, "Ministry of Mysteries"));

            // RETRIEVE (get)
            System.out.println(departmentDao.getAll());

            Optional<Department> optional = departmentDao.getById(1);
            if (optional.isPresent()) {
                System.out.println(optional.get().getDepartmentName());
            }
            else System.out.println("No elements for such requirements");

            // UPDATE
            departmentDao.update(new Department(1, "Ministry of Magic new"));

            if (departmentDao.getById(1).isPresent()) {
                System.out.println(departmentDao.getById(1).get().getDepartmentName());
            }

            // REMOVE
            departmentDao.removeById(1);
            System.out.println(departmentDao.getAll());

            departmentDao.save(new Department(1, "Ministry of Magic")); // back to the previous state

            // table worker
            // CREATE (save)
            WorkerDao workerDao = new WorkerDao(connection);
            workerDao.save(new Worker(1, "Tom", "Zoladz", LocalDate.of(2022,1,1),1));
            workerDao.save(new Worker(2, "Ann", "Szpak", LocalDate.of(2022,5,1),1));
            workerDao.save(new Worker(3, "Tom", "Krajc", LocalDate.of(2023,1,1),2));
            workerDao.save(new Worker(4, "Anna", "Zol", LocalDate.of(2023,4,1),2));

            // RETRIEVE (get)
            System.out.println(workerDao.getAll());
            if (workerDao.getById(3).isPresent())
            {
                System.out.println(workerDao.getById(3).get());
            }
            if (workerDao.getById(6).isPresent()) {
                System.out.println(workerDao.getById(6).get());
            }

            // UPDATE
            workerDao.update(new Worker(4, "Aneczka", "Zoledziowa", LocalDate.of(2023, 4, 1), 2));

            // REMOVE
            workerDao.removeById(1);
            workerDao.getAll().stream().forEach(System.out::println);

            // WORKERFULLINFOSERVICE
            WorkerFullInfoService workerFullInfoService = new WorkerFullInfoService(workerDao, departmentDao);
            System.out.println(workerFullInfoService.presentFullWorkerDataById(2));


            // Closing connection
            // connection.close(); // redundant after putting connection creating into the try brackets

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}