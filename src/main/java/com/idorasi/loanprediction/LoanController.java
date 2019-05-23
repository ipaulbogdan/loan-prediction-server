package com.idorasi.loanprediction;

import com.idorasi.loanprediction.utils.PredictionResponse;
import com.idorasi.loanprediction.utils.TestData;
import com.idorasi.loanprediction.utils.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


import javax.annotation.PostConstruct;


@RestController
public class LoanController {

    private Instances trainingData,testData;
    private Classifier cls = new RandomTree();
    private Evaluation eval;



    @PostMapping("/load")
    public ResponseEntity<String> loadData() {

        try {
            DataSource src = new DataSource("loanPrediction.arff");
            trainingData = src.getDataSet();
            preprocessData();

            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            cls.buildClassifier(trainingData);

            TestData.initiateTestData();
            testData = TestData.testData;

            eval = new Evaluation(trainingData);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Loading dataset failed!");
        }


        return ResponseEntity.ok("Dataset successfully loaded!");
    }


    @GetMapping("/predict")
    public ResponseEntity<PredictionResponse> predictLoan(@RequestBody User user) throws Exception {

        PredictionResponse response;

        testData.add(user.getUserInstance());

        testData.setClassIndex(testData.numAttributes() - 1);


        eval.evaluateModel(cls, testData);


        if (eval.predictions().get(0).predicted() == 0.0) {
            response = new PredictionResponse("Yes", ((NominalPrediction) eval.predictions().get(0)).distribution()[0] * 100);
            return ResponseEntity.ok(response);
        } else if (eval.predictions().get(0).predicted() == 1.0) {
            response = new PredictionResponse("No", ((NominalPrediction) eval.predictions().get(0)).distribution()[1] * 100);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(new PredictionResponse("badRequest",0.0));
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
