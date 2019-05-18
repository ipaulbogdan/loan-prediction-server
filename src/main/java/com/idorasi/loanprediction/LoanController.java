package com.idorasi.loanprediction;

import com.idorasi.loanprediction.utils.PredictionResponse;
import com.idorasi.loanprediction.utils.TestData;
import com.idorasi.loanprediction.utils.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;


import javax.annotation.PostConstruct;



@RestController
public class LoanController {

    private Instances trainingData;
    private Classifier cls = new J48();


    @PostConstruct
    public void loadData() throws Exception {

        DataSource src = new DataSource("loanPrediction.arff");
        trainingData = src.getDataSet();
        TestData.initiateTestData();
    }

    @GetMapping("/predict")
    public ResponseEntity<PredictionResponse> predictLoan(@RequestBody User user) throws Exception {

        PredictionResponse response;

        Instances testData = TestData.testData;

        testData.add(user.getUserInstance());

        preprocessData();


        testData.setClassIndex(testData.numAttributes() - 1);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        cls.buildClassifier(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(cls, TestData.testData);


        if (eval.predictions().get(0).predicted() == 0.0) {
            response = new PredictionResponse("Yes", ((NominalPrediction) eval.predictions().get(0)).distribution()[0] * 100);
            return ResponseEntity.ok(response);
        } else if (eval.predictions().get(0).predicted() == 1.0) {
            response = new PredictionResponse("No ", ((NominalPrediction) eval.predictions().get(0)).distribution()[1] * 100);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(new PredictionResponse("badRequest",0.0));
    }


    private void preprocessData() throws Exception {

        ReplaceMissingValues fixMissing = new ReplaceMissingValues();
        fixMissing.setInputFormat(trainingData);
        trainingData = Filter.useFilter(trainingData, fixMissing);

    }



}
