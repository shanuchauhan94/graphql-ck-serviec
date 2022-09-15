package com.graphql.emp.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GraphQlErrorResponse implements GraphQLError {

    private String message;
    private String errorCode;
    private ErrorType errorType;
    private List<String> invalidQueryFields;
    private List<String> invalidReqKeys;


    public GraphQlErrorResponse(String message, String errorCode, ErrorType errorType, List<String> invalidQueryFields) {
        this.message = message;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.invalidQueryFields = invalidQueryFields;
    }

    @Override
    public String getMessage() {
        if (errorType.compareTo(ErrorType.InvalidSyntax) > 0) {
            return this.message;
        }
        return errorType.name();
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }

    @Override
    public ErrorType getErrorType() {
        return this.errorType;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return new HashMap<>();
    }

    public List<String> getInvalidQueryFields() {

        if (errorType.compareTo(ErrorType.InvalidSyntax) == 0) {
            return invalidQueryFields.stream()
                    .filter(f -> !StringUtils.substringBefore(StringUtils.substringAfter(f, "undefined @ '"), "'").isEmpty())
                    .map(m -> StringUtils.substringBefore(StringUtils.substringAfter(m, "undefined @ '"), "'"))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<String> getInvalidReqKeys() {

        List<String> list = new ArrayList<>();
        List<String> invalidQueryKey = invalidQueryFields.stream()
                .filter(f -> StringUtils.contains(f, "MissingFieldArgument")
                        || StringUtils.contains(f, "UnknownArgument"))
                .map(m -> StringUtils.substringBefore(StringUtils.substringAfter(m, " @ '"), "'")
                        .concat("/")
                        .concat(StringUtils.substringBefore(StringUtils.substringAfter(m, "Unknown field argument "), " @ '")))
                .filter(f -> !StringUtils.substringAfter(f, "/").isEmpty())
                .collect(Collectors.toList());

        List<String> invalidMutationKey = invalidQueryFields.stream()
                .filter(d -> StringUtils.contains(d, "Validation error of type WrongType"))
                .map(m -> StringUtils.substringBefore(StringUtils.substringAfter(m, " @ '"), "'")
                        .concat("/")
                        .concat(StringUtils.substringBefore(StringUtils.substringAfter(m, "': '"), "' @")))
                .filter(f -> !StringUtils.substringAfter(f, "/").isEmpty())
                .collect(Collectors.toList());
        list.addAll(invalidQueryKey);
        list.addAll(invalidMutationKey);

        return list;
    }
}
