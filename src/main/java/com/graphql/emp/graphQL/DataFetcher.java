package com.graphql.emp.graphQL;

import com.graphql.emp.exception.GraphQLExceptionHandler;
import com.graphql.emp.service.EmployeeService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import graphql.servlet.GraphQLObjectMapper;
import graphql.servlet.SimpleGraphQLHttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DataFetcher {

    private final EmployeeService employeeService;

    private SimpleGraphQLHttpServlet graphQLHttpServlet;

    private final GraphQLExceptionHandler exceptionHandler;

    private GraphQL graphQL;

    @Autowired
    public DataFetcher(EmployeeService employeeService, GraphQLExceptionHandler exceptionHandler) {
        this.employeeService = employeeService;
        this.exceptionHandler = exceptionHandler;
    }

    @Bean
    public ServletRegistrationBean<SimpleGraphQLHttpServlet> graphQlServlet() {
        return new ServletRegistrationBean<>(graphQLHttpServlet, "/graphql");
    }

    @Bean
    public GraphQL getGraphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() {

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry definitionRegistry = new TypeDefinitionRegistry();
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/query.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/input.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/type.graphql")));
        definitionRegistry.merge(schemaParser.parse(loadSchemaFile("schema/mutation.graphql")));

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(definitionRegistry, buildRunTimeWiring());

        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        GraphQLObjectMapper mapper = GraphQLObjectMapper.newBuilder().withGraphQLErrorHandler(this.exceptionHandler).build();
        this.graphQLHttpServlet = SimpleGraphQLHttpServlet.newBuilder(graphQLSchema).withObjectMapper(mapper).build();

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
            log.error("schema file read exception {} " + e.getMessage());
        }
        return "";
    }

}
