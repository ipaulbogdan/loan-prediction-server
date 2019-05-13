package com.idorasi.loanprediction.utils;

import weka.core.Attribute;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    public static Instances testData;


    public static void initiateTestData(){
        ArrayList<Attribute> attributes = new ArrayList<>();

        List genderValue = new ArrayList();
        genderValue.add("Male");
        genderValue.add("Female");
        Attribute att1 = new Attribute("Gender",genderValue);
        attributes.add(att1);

        List answer = new ArrayList();
        answer.add("No");
        answer.add("Yes");
        Attribute att2 = new Attribute("Married",answer);
        attributes.add(att2);

        List dependents = new ArrayList();
        dependents.add("0");
        dependents.add("1");
        dependents.add("2");
        dependents.add("3+");
        Attribute att3 = new Attribute("Dependents",dependents);
        attributes.add(att3);

        List graduate = new ArrayList();
        graduate.add("Graduate");
        graduate.add("Not Graduate");
        Attribute att4 = new Attribute("Education",graduate);
        attributes.add(att4);

        Attribute att5 = new Attribute("Self_Employed",answer);
        attributes.add(att5);

        Attribute att6 = new Attribute("ApplicantIncome");
        attributes.add(att6);

        Attribute att7 = new Attribute("CoapplicantIncome");
        attributes.add(att7);

        Attribute att8 = new Attribute("LoanAmount");
        attributes.add(att8);

        Attribute att9 = new Attribute("Loan_Amount_Term");
        attributes.add(att9);

        Attribute att10 = new Attribute("Credit_History");
        attributes.add(att10);

        List area = new ArrayList();
        area.add("Urban");
        area.add("Rural");
        area.add("Semiurban");
        Attribute att11 = new Attribute("Property_Area",area);
        attributes.add(att11);

        List simpleAnswer = new ArrayList();
        simpleAnswer.add("Y");
        simpleAnswer.add("N");
        Attribute att12 = new Attribute("Loan_Status",simpleAnswer);
        attributes.add(att12);

        testData = new Instances("trainData",attributes,25);

    }


    public static Instances getTrainingdata(){
        return testData;
    }
}
