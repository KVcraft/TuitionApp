package com.example.tuitionapp;

public class ResultModel {
    private String name;
    private String result; // will store marks/grade

    public ResultModel(String name, String result) {
        this.name = name;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

