package com.herokuapp.PrivateLearningApp.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileStudentResponse {

    private int id;
    private String name, phone, address, birth_date, gender, email, photo;
    private double latitude, longitude;

    @SerializedName("class")
    @Expose
    private String gradeStudy;

    public ProfileStudentResponse(int id, String name, String phone, String address, String birth_date, String gender, String email, String photo, double latitude, double longitude, String gradeStudy) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birth_date = birth_date;
        this.gender = gender;
        this.email = email;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gradeStudy = gradeStudy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getGradeStudy() {
        return gradeStudy;
    }

    public void setGradeStudy(String gradeStudy) {
        this.gradeStudy = gradeStudy;
    }
}
