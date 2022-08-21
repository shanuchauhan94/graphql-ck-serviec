package com.graphql.emp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphQlRequest {

    private String query;
    private String operationName;
    private Map<String, Object> getVariables;

}
