package com.waes.apigateway.exception;

import feign.Response;
import feign.codec.ErrorDecoder;

import java.util.NoSuchElementException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()) {
            case 402:
                return new IllegalArgumentException("Unprocessable Entity.");
            case 404:
                return new NoSuchElementException("Entity not found.");
            case 406:
                return new IllegalStateException("Entity is incomplete for operation, please check.");
            default:
                return new Exception("Internal error");
        }
    }
}