package com.graphql.emp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmployeeCompositeKey key = (EmployeeCompositeKey) o;

        return new EqualsBuilder().append(email, key.email).append(salary, key.salary).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(email).append(salary).toHashCode();
    }
}
