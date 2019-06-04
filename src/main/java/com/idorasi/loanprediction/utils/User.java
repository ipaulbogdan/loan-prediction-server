package com.idorasi.loanprediction.utils;

import weka.core.*;

public class User {

    private String gender;
    private String married;
    private int dependents;
    private String education;
    private String self_employed;
    private int income;
    private int coIncome;
    private int loanAmount;
    private int loanTerm;
    private int creditHistory;
    private String area;
    private String loanStatus;

    public User(String gender, String married, int dependents, String education, String self_employed, int income, int coIncome, int loanAmount, int loanTerm, int creditHistory, String area) {
        this.gender = gender;
        this.married = married;
        this.dependents = dependents;
        this.education = education;
        this.self_employed = self_employed;
        this.income = income;
        this.coIncome = coIncome;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.creditHistory = creditHistory;
        this.area = area;
    }

    @Override
    public String toString() {
        return gender+","+married+","+dependents+","+education+","+self_employed+","+income+","+coIncome+","+loanAmount+","+loanTerm+","+creditHistory+","+area+","+loanStatus;
    }

    public Instance getUserInstance(){

        Instance inst = new DenseInstance(12);
        inst.setDataset(TestData.getTrainingdata());

        inst.setValue(0,gender);
        inst.setValue(1,married);
        if(dependents<4)
            inst.setValue(2,dependents);
        else
            inst.setValue(2,"3+");
        inst.setValue(3,education);
        inst.setValue(4,self_employed);
        inst.setValue(5,income);
        inst.setValue(6,coIncome);
        inst.setValue(7,loanAmount);
        inst.setValue(8,loanTerm);
        if(creditHistory >=0)
            inst.setValue(9,creditHistory);
        inst.setValue(10,area);

        return inst;
    }


}
