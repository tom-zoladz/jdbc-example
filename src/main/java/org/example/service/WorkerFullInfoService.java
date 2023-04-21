package org.example.service;

import org.example.dao.DepartmentDao;
import org.example.dao.WorkerDao;
import org.example.model.Worker;

import java.sql.SQLException;
import java.util.Optional;

// Purpose of this class is to return worker information including department name
public class WorkerFullInfoService {

    private final WorkerDao workerDao;
    private final DepartmentDao departmentDao;


    public WorkerFullInfoService(WorkerDao workerDao, DepartmentDao departmentDao) {
        this.workerDao = workerDao;
        this.departmentDao = departmentDao;
    }


    public Optional<WorkerWithDepartment> presentFullWorkerDataById(int id) throws SQLException {
        if (workerDao.getById(id).isPresent()) {
            Worker worker = workerDao.getById(id).get();

            if (departmentDao.getById(worker.getDepartmentId()).isPresent()) {
                return Optional.of(new WorkerWithDepartment(
                        worker.getWorkerId(),
                        worker.getFirstName(),
                        worker.getLastName(),
                        worker.getHireDate(),
                        departmentDao.getById(worker.getDepartmentId()).get().getDepartmentName()
                ));
            }
        }

        return Optional.empty();
    }

}