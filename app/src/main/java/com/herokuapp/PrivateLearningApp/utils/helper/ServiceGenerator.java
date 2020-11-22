package com.herokuapp.PrivateLearningApp.utils.helper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String BASE_URL = "https://proyek-si-lesprivat.herokuapp.com";

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;
    public static Retrofit retrofit() { return retrofit; }

    private static final HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient.Builder client =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass, String token) {
        if (client.interceptors().contains(logging) || token != null) {
            client.addInterceptor(new Authorization(token));
        }
        client.addInterceptor(logging);
        builder.client(client.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }
}
