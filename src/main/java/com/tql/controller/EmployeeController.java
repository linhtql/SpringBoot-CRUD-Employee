package com.tql.controller;

import com.tql.entities.Employee;
import com.tql.entities.ResponseObject;
import com.tql.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;


    //get all employees
    @GetMapping("")
    List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    //get detail employee
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
            Optional<Employee> foundEmployee = employeeService.findById(id);
            return(foundEmployee.isPresent()) ?
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Query employee successfully !", foundEmployee)
                    )
            :
                    ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("failed", "Cannot find employee with id ="+id, "")
                    );

    }

    //insert new employee
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertEmployee(@RequestBody Employee newEmployee) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "insert employee successfully !", employeeService.save(newEmployee))
        );
    }

    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    Employee updateEmployee = employeeService.findById(id)
            .map(employee -> {
                employee.setId(newEmployee.getId());
                employee.setFirstName(newEmployee.getFirstName());
                employee.setLastName(newEmployee.getLastName());
                employee.setAddress(newEmployee.getAddress());
                employee.setEmail(newEmployee.getEmail());
                employee.setPhone(newEmployee.getPhone());
                employee.setBirthOfDay(newEmployee.getBirthOfDay());

                return employeeService.save(employee);
            }).orElseGet(()->{
                return employeeService.save(newEmployee);
            });

    return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "update employee successfully !", newEmployee)
    );
    }

    //delete a employee
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteEmployee(@PathVariable Long id) {
        boolean exists = employeeService.existsById(id);

        if(exists) {
            employeeService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "delete employee successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Cannot find employee to delete", "")
        );
    }


}
