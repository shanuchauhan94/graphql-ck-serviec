package com.graphql.emp.service;

import com.google.gson.Gson;
import com.graphql.emp.model.Employee;
import com.graphql.emp.model.EmployeeRecord;
import com.graphql.emp.model.EmployeeResponse;
import com.graphql.emp.repository.EmployeeRepository;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }


    public DataFetcher<List<Employee>> getEmployeeRecord() {
        log.info("Get employee service executing.");
        return environment -> repository.findAll();
    }

    public DataFetcher<EmployeeResponse> saveEmployeeRecord() {
        log.info("save employee service executing.");
        return environment -> {
            LinkedHashMap<String, Object> emp = environment.getArgument("employee");
            EmployeeRecord employeeRecord = new Gson().fromJson(new Gson().toJson(emp), EmployeeRecord.class);
            Employee employee = new Employee(employeeRecord);
            return new EmployeeResponse(repository.save(employee));
        };

    }
}
