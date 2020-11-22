package com.herokuapp.PrivateLearningApp.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.herokuapp.PrivateLearningApp.model.request.StudentUpdateRequest;
import com.herokuapp.PrivateLearningApp.model.request.TeacherUpdateRequest;
import com.herokuapp.PrivateLearningApp.model.response.LogoutResponse;
import com.herokuapp.PrivateLearningApp.model.response.ProfileStudentResponse;
import com.herokuapp.PrivateLearningApp.model.response.ProfileTeacherResponse;
import com.herokuapp.PrivateLearningApp.model.response.Responses;
import com.herokuapp.PrivateLearningApp.utils.ApiInterface;
import com.herokuapp.PrivateLearningApp.utils.helper.ServiceGenerator;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<ProfileStudentResponse> studentResponse = new MutableLiveData<>();
    private final MutableLiveData<ProfileTeacherResponse> teacherResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<ProfileStudentResponse> getStudentResponse() { return studentResponse; }

    public LiveData<ProfileTeacherResponse> getTeacherResponse() { return teacherResponse; }

    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public LiveData<String> getMessage() { return message; }

    public void getProfileStudent(String token, int id) {
        isLoading.setValue(true);
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, token);
        Call<ProfileStudentResponse> call = request.profileStudent(id);
        call.enqueue(new Callback<ProfileStudentResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProfileStudentResponse> call, @NotNull Response<ProfileStudentResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    studentResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileStudentResponse> call, @NotNull Throwable t) {
                Log.e("Profile", t.getMessage());
            }
        });
    }

    public void getProfileTeacher(String token, int id) {
        isLoading.setValue(true);
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, token);
        Call<ProfileTeacherResponse> call = request.profileTeach(id);
        call.enqueue(new Callback<ProfileTeacherResponse>() {
            @Override
            public void onResponse(@NotNull Call<ProfileTeacherResponse> call, @NotNull Response<ProfileTeacherResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    teacherResponse.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ProfileTeacherResponse> call, @NotNull Throwable t) {
                Log.e("Profile", t.getMessage());
            }
        });
    }

    public void logout(String token, int id, String user) {
        isLoading.setValue(true);
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, token);
        Call<LogoutResponse> call;
        if (user.equals("teacher")) {
            call = request.logOutTeacher(id);
        } else {
            call = request.logOutStudent(id);
        }
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(@NotNull Call<LogoutResponse> call, @NotNull Response<LogoutResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    Log.e("Logout", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<LogoutResponse> call, @NotNull Throwable t) {
                Log.e("Logout Failed", t.getMessage());
            }
        });
    }

    public void updateAccountTeacher(TeacherUpdateRequest teacherUpdateRequest, String token, int id) {
        isLoading.setValue(true);
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, token);
        Call<Responses> call = request.updateTeacher(teacherUpdateRequest, id);
        call.enqueue(new Callback<Responses>() {
            @Override
            public void onResponse(@NotNull Call<Responses> call, @NotNull Response<Responses> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    Log.e("Update has problem", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Responses> call, @NotNull Throwable t) {
                Log.e("Update Failed", t.getMessage());
            }
        });
    }

    public void updateAccountStudent(StudentUpdateRequest studentUpdateRequest, String token, int id) {
        isLoading.setValue(true);
        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, token);
        Call<Responses> call = request.updateStudent(studentUpdateRequest, id);
        call.enqueue(new Callback<Responses>() {
            @Override
            public void onResponse(@NotNull Call<Responses> call, @NotNull Response<Responses> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    message.postValue(response.body().getMessage());
                } else {
                    Log.e("Update has problem", response.message());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Responses> call, @NotNull Throwable t) {
                Log.e("Update failed", t.getMessage());
            }
        });
    }
}
