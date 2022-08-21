package com.graphql.emp.controller;

import com.graphql.emp.model.GraphQlRequest;
import graphql.ExecutionInput;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GraphQLController {

    private final GraphQL graphQL;

    @Autowired
    public GraphQLController(GraphQL graphQL) {
        this.graphQL = graphQL;
    }

    @PostMapping("/graphql")
    public Map<String, Object> execute(@RequestBody GraphQlRequest qlRequest) {

        return graphQL.execute(ExecutionInput.newExecutionInput()
                .query(qlRequest.getQuery())
                .operationName(qlRequest.getOperationName())
                .variables(qlRequest.getGetVariables())
                .build()).toSpecification();
    }
}
