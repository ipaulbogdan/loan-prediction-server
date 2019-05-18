package com.idorasi.loanprediction.utils;

public class PredictionResponse {

    private String response;
    private double accuracy;

    public PredictionResponse(String response, double accuracy) {
        this.response = response;
        this.accuracy = accuracy;
    }


    public String getResponse() {
        return response;
    }

    public double getAccuracy() {
        return accuracy;
    }

    @Override
    public String toString() {
        return "PredictionResponse{" +
                "response='" + response + '\'' +
                ", accuracy=" + accuracy +
                '}';
    }
}
