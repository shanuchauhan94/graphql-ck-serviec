package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class EmployeeCompositeKey implements Serializable {

    private String email;
    private Integer salary;

    public EmployeeCompositeKey(String email, Integer salary) {
        this.email = email;
        this.salary = salary;
    }
}
