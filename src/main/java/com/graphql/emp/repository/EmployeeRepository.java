package com.graphql.emp.repository;

import com.graphql.emp.model.Employee;
import com.graphql.emp.model.EmployeeCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, EmployeeCompositeKey> {
}
