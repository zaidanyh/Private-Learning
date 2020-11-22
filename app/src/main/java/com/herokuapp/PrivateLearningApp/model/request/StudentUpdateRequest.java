package com.herokuapp.PrivateLearningApp.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentUpdateRequest {

    private String name, phone, address, gender, birth_date, latitude, longitude;

    @SerializedName("class")
    @Expose
    private String gradeStudy;

    public StudentUpdateRequest(String name, String phone, String address, String gender, String birth_date, String latitude, String longitude, String gradeStudy) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birth_date = birth_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gradeStudy = gradeStudy;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGradeStudy() {
        return gradeStudy;
    }

    public void setGradeStudy(String gradeStudy) {
        this.gradeStudy = gradeStudy;
    }
}
