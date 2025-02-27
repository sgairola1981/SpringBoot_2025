package com.vayam.demo;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

class Employee {
    private String name;
    private Department department;
    private double salary;

    public Employee(String name, Department department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }
}

enum Department {
    IT, HR, SALES, FINANCE
}

public class EmployeeSalaryGrouping {
    public static void main(String[] args) {
        // Sample list of employees
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", Department.IT, 60000),
                new Employee("Bob", Department.IT, 75000),
                new Employee("Charlie", Department.HR, 50000),
                new Employee("David", Department.HR, 55000),
                new Employee("Eve", Department.SALES, 70000),
                new Employee("Frank", Department.SALES, 72000),
                new Employee("Grace", Department.FINANCE, 90000)
        );

        // Grouping employees by department and calculating the average salary
        Map<Department, Double> averageSalaryByDepartment = employees.stream()
                .collect(groupingBy(
                        Employee::getDepartment,
                        averagingDouble(Employee::getSalary)
                ));

        // Print the results
        averageSalaryByDepartment.forEach((dept, avgSalary) ->
                System.out.println(dept + " -> Average Salary: $" + avgSalary)
        );


    }
}
