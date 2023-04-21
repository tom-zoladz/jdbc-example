package org.example.service;

import java.time.LocalDate;

public record WorkerWithDepartment(int workerId, String firstName, String lastName, LocalDate hireDate, String departmentName) {

}
