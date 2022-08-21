package com.graphql.emp.exception;

import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.GenericGraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQLExceptionHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errorList) {

        System.err.println("*********************************************************************");
        List<GraphQLError> finalErrors = new ArrayList<>();
        List<GraphQLError> clientErrors = this.filterGraphQLErrors(errorList);
        List<GraphQLError> internalError = errorList.stream()
                .filter(this::isInternalError)
                .map(GraphQLErrorAdaptor::new)
                .collect(Collectors.toList());

        if (clientErrors.size() + internalError.size() < errorList.size()) {
            clientErrors.add(new GenericGraphQLError("fond error while executing query"));
            errorList.stream().filter((error) -> !this.isClientError(error))
                    .forEach((error) -> {
                        if (error instanceof Throwable) {
                            System.err.println("error executing query " + (Throwable) error);
                        } else {
                            System.err.println("*****ERROR******* " + error.getMessage());
                        }
                    });
        }
        finalErrors.addAll(clientErrors);
        finalErrors.addAll(internalError);

        return finalErrors;
    }

    private List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errorList) {
        return errorList.stream().filter(this::isClientError).collect(Collectors.toList());
    }

    private boolean isClientError(GraphQLError error) {
        return !(error instanceof ExceptionWhileDataFetching) && !(error instanceof Throwable);
    }

    private boolean isInternalError(GraphQLError error) {
        return (error instanceof ExceptionWhileDataFetching) &&
                (((ExceptionWhileDataFetching) error).getException() instanceof CustomGraphQLException);
    }
}
