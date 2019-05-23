package com.idorasi.loanprediction.utils;

public class PredictionResponse {

    private String response;
    private String accuracy;

    public PredictionResponse(String response, double accuracy) {
        this.response = response;
        this.accuracy = accuracy + "%";
    }


    public String getResponse() {
        return response;
    }

    public String getAccuracy() {
        return accuracy;
    }

    @Override
    public String toString() {
        return "PredictionResponse{" +
                "response='" + response + '\'' +
                ", accuracy=" + accuracy + "%"+
                '}';
    }
}
