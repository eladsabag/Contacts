package com.example.contacts.objects;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("gender")
    private String gender;


    public Result(String name) {
        this.gender = name;
    }

    public String getGender() {
        return gender;
    }
}