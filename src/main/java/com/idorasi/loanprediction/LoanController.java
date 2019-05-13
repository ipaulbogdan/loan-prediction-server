package com.idorasi.loanprediction;



import com.idorasi.loanprediction.utils.TestData;
import com.idorasi.loanprediction.utils.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


import javax.annotation.PostConstruct;



@RestController
public class LoanController {

    private Instances trainingData,testData;
    private Classifier cls = new J48();


    @PostConstruct
    public void loadData() throws Exception {

        DataSource src = new DataSource("C:\\Users\\idoMachine\\Desktop\\loanprediction\\src\\main\\resources\\loanPrediction.arff");
        trainingData = src.getDataSet();
        TestData.initiateTestData();
    }

    @GetMapping("/predict")
    public String predictLoan(@RequestBody User user) throws Exception {
        String loanPrediction;

        TestData.testData.add(user.getUserInstance());

        TestData.testData.setClassIndex(TestData.testData.numAttributes() - 1);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        cls.buildClassifier(trainingData);
        // evaluate classifier and print some statistics
        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(cls, TestData.testData);

        System.out.println();

        if(eval.predictions().get(0).predicted() == 0.0)
                loanPrediction = "Yes "+((NominalPrediction)eval.predictions().get(0)).distribution()[0]*100+"%";
        else
                loanPrediction = "No "+((NominalPrediction)eval.predictions().get(0)).distribution()[1]*100+"%";


        return loanPrediction;

    }

}
