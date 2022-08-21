package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeResponse {

    private String email;
    private Integer salary;

    public EmployeeResponse(Employee employee) {
        this.email = employee.getCk().getEmail();
        this.salary = employee.getCk().getSalary();
    }
}
