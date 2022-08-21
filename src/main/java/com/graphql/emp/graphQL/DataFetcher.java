package com.graphql.emp.graphQL;

import com.graphql.emp.service.EmployeeService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataFetcher {

    private final EmployeeService employeeService;

    @Autowired
    public DataFetcher(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Bean
    public GraphQL graphQL() {

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry definitionRegistry = new TypeDefinitionRegistry();
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/query.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/input.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/type.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/mutation.graphql")));

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(definitionRegistry, buildRunTimeWiring());

        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    public RuntimeWiring buildRunTimeWiring() {

        Map<String, graphql.schema.DataFetcher> queryDataFetcherMap = new HashMap<>();
        Map<String, graphql.schema.DataFetcher> mutationDataFetcherMap = new HashMap<>();

        mutationDataFetcherMap.put("saveEmployee", employeeService.saveEmployeeRecord());
        queryDataFetcherMap.put("employeeRecords", employeeService.getEmployeeRecord());
        return RuntimeWiring.newRuntimeWiring().type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetchers(mutationDataFetcherMap)).type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(queryDataFetcherMap)).build();
    }

    private String loadSchemaFile(String schemaName) {

        URL url = getClass().getClassLoader().getResource(schemaName);
        try {
            if (url != null) {
                return IOUtils.toString(url, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.err.println("schema file read exception {} " + e.getMessage());
        }
        return "";
    }

}
