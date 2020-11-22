package com.herokuapp.PrivateLearningApp.utils;

import com.herokuapp.PrivateLearningApp.model.request.LoginRequest;
import com.herokuapp.PrivateLearningApp.model.request.RegisterRequest;
import com.herokuapp.PrivateLearningApp.model.request.StudentUpdateRequest;
import com.herokuapp.PrivateLearningApp.model.request.TeacherUpdateRequest;
import com.herokuapp.PrivateLearningApp.model.response.LoginResponse;
import com.herokuapp.PrivateLearningApp.model.response.LogoutResponse;
import com.herokuapp.PrivateLearningApp.model.response.ProfileStudentResponse;
import com.herokuapp.PrivateLearningApp.model.response.ProfileTeacherResponse;
import com.herokuapp.PrivateLearningApp.model.response.Responses;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("/api/teacher/register")
    Call<Responses> registerTeacher(@Body RegisterRequest request);

    @POST("/api/teacher/login")
    Call<LoginResponse> loginTeach(@Body LoginRequest request);

    @GET("/api/teacher/{teacher}")
    Call<ProfileTeacherResponse> profileTeach(@Path("teacher") int id);

    @PUT("/api/teacher/{teacher}")
    Call<Responses> updateTeacher(@Body TeacherUpdateRequest request, @Path("teacher") int id);

    @POST("/api/teacher/{teacher}/logout")
    Call<LogoutResponse> logOutTeacher(@Path("teacher") int id);



    @POST("/api/student/register")
    Call<Responses> registerStudent(@Body RegisterRequest request);

    @POST("/api/student/login")
    Call<LoginResponse> loginStud(@Body LoginRequest request);

    @GET("/api/student/{student}")
    Call<ProfileStudentResponse> profileStudent(@Path("student") int id);

    @PUT("/api/student/{student}")
    Call<Responses> updateStudent(@Body StudentUpdateRequest request, @Path("student") int id);

    @POST("/api/student/{student}/logout")
    Call<LogoutResponse> logOutStudent(@Path("student") int id);
}
