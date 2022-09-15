package com.graphql.emp.exception;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GenericGraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@Component
public class GraphQLExceptionHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors) {

        List<GraphQLError> clientErrors = this.filterGraphQLErrors(errors);
        if (clientErrors.size() < errors.size()) {
            clientErrors.add(new GenericGraphQLError("Internal Server Error(s) while executing query"));
            errors.stream().filter(error -> !this.isClientError(error)).forEach(error -> {
                if (error instanceof Throwable) {
                    log.error("Error executing query!", (Throwable) error);
                } else {
                    log.error("Error executing query ({}): {}", error.getClass().getSimpleName(), error.getMessage());
                }
            });
        }
        return errors;
    }

    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream().filter(this::isClientError).collect(Collectors.toList());
    }

    protected boolean isClientError(GraphQLError error) {
        if (error instanceof ExceptionWhileDataFetching) {
            return ((ExceptionWhileDataFetching) error).getException() instanceof GraphQLError;
        } else {
            return !(error instanceof Throwable);
        }
    }
}
