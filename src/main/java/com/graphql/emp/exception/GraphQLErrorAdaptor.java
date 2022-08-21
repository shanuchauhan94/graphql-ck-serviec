package com.graphql.emp.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

public class GraphQLErrorAdaptor implements GraphQLError {

    private final GraphQLError graphQLError;

    public GraphQLErrorAdaptor(GraphQLError graphQLError) {
        this.graphQLError = graphQLError;
    }

    @Override
    public String getMessage() {
        return graphQLError.getMessage();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return graphQLError.getLocations();
    }

    @Override
    public ErrorType getErrorType() {
        return graphQLError.getErrorType();
    }

    @Override
    public List<Object> getPath() {
        return GraphQLError.super.getPath();
    }
}
