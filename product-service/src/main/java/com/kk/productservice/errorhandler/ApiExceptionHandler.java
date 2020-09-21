package com.kk.productservice.errorhandler;

import com.kk.productservice.errorhandler.exception.ProductExistsException;
import com.kk.productservice.errorhandler.exception.ProductNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles exceptions occurring in the API and sends an error response
 */

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * **TODO**
     * Create a slf4j Logger for logging messages to standard output
     */

    /**
     * Exception handler for ProductExistsException
     * **TODO**
     * Log the below `error` message in the method
     * "Building error response for ProductNotFoundException"
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFoundException(ProductNotFoundException exception, WebRequest request) {
        logger.error("Building error response for ProductNotFoundException", exception);
        ApiErrorResponse errorResponse = buildErrorResponse(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    /**
     * Exception handler for ProductExistsException
     * **TODO**
     * Log the below `error` message in the method
     * "Building error response for ProductExistsException"
     */
    @ExceptionHandler(ProductExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleProductExistsException(ProductExistsException exception, WebRequest request) {
        logger.error("Building error response for ProductExistsException", exception);
        ApiErrorResponse errorResponse = buildErrorResponse(exception.getLocalizedMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    /**
     * Exception handler for Invalid data in Http Request
     * **TODO**
     * Log the below `error` message in the method
     * "Building error response for invalid request parameters"
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> validationMessages = new ArrayList<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            validationMessages.add(error.getDefaultMessage());
        }
        String message = "Validation of Request failed";
        ApiErrorResponse errorResponse = buildErrorResponse(message, HttpStatus.BAD_REQUEST);
        logger.error("Building error response for invalid request parameters", exception);
        errorResponse.getErrors().addAll(validationMessages);
        return handleExceptionInternal(exception, errorResponse, headers, errorResponse.getStatus(), request);
    }

    /**
     * Exception handler for exceptions other than the above
     * **TODO**
     * Log the below `error` message in the method
     * "Building error response for exceptions"
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllException(Exception exception, WebRequest request) {
        logger.error("Building error response for exceptions", exception);
        ApiErrorResponse errorResponse = buildErrorResponse(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    /**
     * Building standard error response for the exceptions in the API
     */
    private ApiErrorResponse buildErrorResponse(String message, HttpStatus httpStatus) {
        return ApiErrorResponse.builder()
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .message(message)
                .build();

    }
}