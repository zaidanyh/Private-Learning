package com.herokuapp.PrivateLearningApp.ui.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.herokuapp.PrivateLearningApp.model.request.RegisterRequest;
import com.herokuapp.PrivateLearningApp.model.response.Responses;
import com.herokuapp.PrivateLearningApp.utils.ApiInterface;
import com.herokuapp.PrivateLearningApp.utils.helper.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<String> getMessage() { return message; }

    public void registerTeacher(RegisterRequest registerRequest) {
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, "");
        Call<Responses> call = request.registerTeacher(registerRequest);
        call.enqueue(new Callback<Responses>() {
            @Override
            public void onResponse(@NotNull Call<Responses> call, @NotNull Response<Responses> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    Log.e("Registration", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Responses> call, @NotNull Throwable t) {
                Log.e("Registration failed", t.getMessage());
            }
        });
    }

    public void registerStudent(RegisterRequest registerRequest) {
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, "");
        Call<Responses> call = request.registerStudent(registerRequest);
        call.enqueue(new Callback<Responses>() {
            @Override
            public void onResponse(@NotNull Call<Responses> call, @NotNull Response<Responses> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    Log.e("Registration", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Responses> call, @NotNull Throwable t) {
                Log.e("Registration failed", t.getMessage());
            }
        });
    }
}
