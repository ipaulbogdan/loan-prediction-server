package com.idorasi.loanprediction;

import com.idorasi.loanprediction.utils.PredictionResponse;
import com.idorasi.loanprediction.utils.TestData;
import com.idorasi.loanprediction.utils.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import javax.annotation.PostConstruct;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class LoanController {

    private Instances trainingData,testData;
    private Classifier cls = new RandomTree();
    private Evaluation eval;



    @PostConstruct
    public ResponseEntity<String> loadData() {

        try {
            DataSource src = new DataSource("loanPrediction.arff");
            trainingData = src.getDataSet();
            preprocessData();

            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            cls.buildClassifier(trainingData);

            eval = new Evaluation(trainingData);
        }catch (Exception e){
            return new ResponseEntity<>("Failed to load data set", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>("Data set loaded successfully",HttpStatus.OK);
    }


    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predictLoan(@RequestBody User user) throws Exception {

        PredictionResponse response;

        TestData.initiateTestData();
        testData = TestData.testData;
        testData.add(user.getUserInstance());

        testData.setClassIndex(testData.numAttributes() - 1);


        eval.evaluateModel(cls, testData);

/*
        if (eval.predictions().get(0).predicted() == 0.0) {
            response = new PredictionResponse("Yes", ((NominalPrediction) eval.predictions().get(0)).distribution()[0] * 100);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } else if (eval.predictions().get(0).predicted() == 1.0) {
            response = new PredictionResponse("No", ((NominalPrediction) eval.predictions().get(0)).distribution()[1] * 100);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }*/

        return new ResponseEntity<>(new PredictionResponse("Unknown",0.0),HttpStatus.BAD_REQUEST);
    }


    private void preprocessData() throws Exception {

        ReplaceMissingValues fixMissing = new ReplaceMissingValues();
        fixMissing.setInputFormat(trainingData);
        trainingData = Filter.useFilter(trainingData, fixMissing);

    }


    @GetMapping("/view/tree")
    public String printDecisionTree(){
        return cls.toString();
    }



}
