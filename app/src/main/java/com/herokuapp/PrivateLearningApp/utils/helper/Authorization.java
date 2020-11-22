package com.herokuapp.PrivateLearningApp.utils.helper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class Authorization implements Interceptor {
    private final String token;

    public Authorization(String token) {
        this.token = token;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder()
                .header("Authorization", "Bearer "+token);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
