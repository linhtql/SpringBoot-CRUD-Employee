package com.tql.service;

import com.tql.entities.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    boolean existsById(Long id);
    void deleteById(Long id);

}
