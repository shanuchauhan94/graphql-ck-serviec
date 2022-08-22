package com.graphql.emp.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class GraphQLErrorAspect {

    @Pointcut(value = "execution(public * com.graphql.emp.exception.GraphQLExceptionHandler..*(..))")
    public void getGraphqlErrorHandler() {
    }

    @Around("getGraphqlErrorHandler()")
    public Object graphqlQueryValidationHandler(ProceedingJoinPoint joinPoint) throws Throwable {

        List<GraphQLError> errorList = (List<GraphQLError>) joinPoint.proceed();
        List<GraphQlErrorResponse> responseList = new ArrayList<>();

        String errors = errorList.get(0).getMessage();
        List<String> expList = errorList.stream().map(GraphQLError::getMessage).collect(Collectors.toList());
        String errorCode = "400";
        ErrorType errorType = ErrorType.ValidationError;
        if (StringUtils.isNoneBlank(errors)) {
            if (errors.contains("Invalid object name")) {
                errorCode = "404";
                errorType = ErrorType.DataFetchingException;
            } else if (errors.contains("Invalid column name")) {
                errorCode = "404";
                errorType = ErrorType.DataFetchingException;
            } else if (errors.contains("Validation error")) {
                errorCode = "400";
                errorType = ErrorType.InvalidSyntax;
            }
            log.info("exception messages {} ", errors);

            String message = Optional.of(errors)
                    .filter(f -> !f.startsWith("Internal"))
                    .map(m -> StringUtils.substringAfter(errors, ":"))
                    .orElse(errors);
            GraphQlErrorResponse errorResp = new GraphQlErrorResponse(message, errorCode, errorType, expList);
            responseList.add(errorResp);
            return responseList;
        }
        return responseList;
    }
}
