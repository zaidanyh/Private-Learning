package com.herokuapp.PrivateLearningApp.utils.helper;

import com.herokuapp.PrivateLearningApp.model.response.ErrorResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ApiError {

    public static ErrorResponse parseError(Response<?> response) {
        Converter<ResponseBody, ErrorResponse> converter =
                ServiceGenerator.retrofit()
                .responseBodyConverter(ErrorResponse.class, new Annotation[0]);
        ErrorResponse errorResponse = null;
        try {
            if (response.errorBody() != null) {
                errorResponse = converter.convert(response.errorBody());
            }
        } catch (IOException e) {
            return new ErrorResponse();
        }
        return errorResponse;
    }
}
