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
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import javax.annotation.PostConstruct;


@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/loan")
@RestController
public class LoanController {

    private Instances trainingData,testData;
    private Classifier cls = new RandomForest();
    private Evaluation eval;
    private int predictionsCount=0;



    @PostConstruct
    public String loadData() {

        try {
            DataSource src = new DataSource("loanPrediction.arff");
            trainingData = src.getDataSet();
            preprocessData();

            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            cls.buildClassifier(trainingData);

            eval = new Evaluation(trainingData);
        }catch (Exception e){
            System.out.println("\n\tLoading the data set failed!\n");
            return "Failure";
        }


        System.out.println("\n\tData set loaded successfully\n");
        return "Success";
    }


    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predictLoan(@RequestBody User user) throws Exception {

        PredictionResponse response;

        TestData.initiateTestData();
        testData = TestData.testData;
        testData.add(user.getUserInstance());

        testData.setClassIndex(testData.numAttributes() - 1);


        eval.evaluateModel(cls, testData);


        if (eval.predictions().get(predictionsCount).predicted() == 0.0) {
            response = new PredictionResponse("Yes", ((NominalPrediction) eval.predictions().get(predictionsCount)).distribution()[0] * 100);
            predictionsCount++;
            return new ResponseEntity<>(response,HttpStatus.OK);
        } else if (eval.predictions().get(predictionsCount).predicted() == 1.0) {
            response = new PredictionResponse("No", ((NominalPrediction) eval.predictions().get(predictionsCount)).distribution()[1] * 100);
            predictionsCount++;
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
