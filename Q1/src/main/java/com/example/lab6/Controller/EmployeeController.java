package com.example.lab6.Controller;

import com.example.lab6.ApiResponse;
import com.example.lab6.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    ArrayList<Employee> employees = new ArrayList<Employee>();

    @GetMapping("/getAll")
    public ResponseEntity<ArrayList<Employee>> getAllEmployees() {
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@Valid @RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(errorMessage));
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index, @Valid@RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String errorMessage = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(errorMessage));
        }
        if (index < 0 || index >= employees.size()) {
            return ResponseEntity.status(400).body(new ApiResponse("index out of bounds"));
        }
        employees.set(index, employee);
        return ResponseEntity.status(200).body(new ApiResponse("updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                employees.remove(employee);
                return ResponseEntity.status(200).body(new ApiResponse("employee deleted successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("No such employee"));
    }

    @GetMapping("/searchByPosition/{position}")
    public ResponseEntity searchByPosition(@PathVariable String position) {
        ArrayList<Employee> employeeByPosition = new ArrayList<>();
        if (position.equals("supervisor") || position.equals("coordinator")) {
            for (Employee e : employees) {
                if (e.getPosition().equals(position)) {
                    employeeByPosition.add(e);
                }
            }
            return ResponseEntity.status(200).body(employeeByPosition);
        }
        return ResponseEntity.status(400).body(new ApiResponse("the value must be either coordinator or supervisor"));
    }

    @GetMapping("/getAgeByRange/{min}/{max}")
    public ResponseEntity getAgeByRange(@PathVariable int min, @PathVariable int max) {
        ArrayList<Employee> employeeByRange = new ArrayList<>();
        if (min > max || min < 0 || max > 200) {
            return ResponseEntity.status(400).body(new ApiResponse("the first value must be smaller then scened, and the values must be between 1 and 200"));
        }
        for (Employee e : employees) {
            if (e.getAge() >= min && e.getAge() <= max) {
                employeeByRange.add(e);
            }
        }
        return ResponseEntity.status(200).body(employeeByRange);
    }

    @PutMapping("/applyForAnnualLeave/{id}")
    public ResponseEntity applyForAnnualLeave(@PathVariable String id) {
        for (Employee e : employees) {
            if (e.getId().equals(id) && !e.isOnLeave() && e.getAnnualLeave() > 0){
                e.setAnnualLeave(e.getAnnualLeave() - 1);
                e.setOnLeave(true);
                return ResponseEntity.status(200).body(new ApiResponse("annual leave granted"));
            }
            else
                return ResponseEntity.status(400).body(new ApiResponse("the employee is on the leave or don't have annual leave credit"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no employee found"));
    }

    @GetMapping("/getEmployeesWithNoAnnualLeave")
    public ResponseEntity getEmployeesWithNoAnnualLeave() {
        ArrayList<Employee> employeesWithNoAnnualLeave = new ArrayList<>();
        if (employees.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there's no employees"));
        }
        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0){
                employeesWithNoAnnualLeave.add(e);
            }
        }
        if (employeesWithNoAnnualLeave.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there's no employees"));
        }
        else
            return ResponseEntity.status(200).body(employeesWithNoAnnualLeave);
    }

    @PutMapping("/promote/{userID}/{targetID}")
    public ResponseEntity promote(@PathVariable String userID, @PathVariable String targetID) {
        for (Employee e : employees) {
            if (e.getId().equals(userID) && e.getPosition().equals("supervisor")) {
                for (Employee e2 : employees) {
                    if (e2.getId().equals(targetID) && e.getAge() >= 30 && !e.isOnLeave()) {
                        e2.setPosition("supervisor");
                        return ResponseEntity.status(200).body(new ApiResponse("the employee is promoted"));
                    }
                }
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("you are not a supervisor or the employee dose not satisfied the qualification"));
    }

}
