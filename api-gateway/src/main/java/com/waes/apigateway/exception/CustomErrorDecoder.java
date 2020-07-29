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
            default:
                return new Exception("Internal error");
        }
    }
}