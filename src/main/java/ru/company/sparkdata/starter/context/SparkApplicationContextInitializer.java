package ru.company.sparkdata.starter.context;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import ru.company.sparkdata.starter.SparkRepository;

public class SparkApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        registerSparkBean(context);
        String packagesToScan = context.getEnvironment().getProperty("spark.packages-to-scan");
        Reflections scanner = new Reflections(packagesToScan);
        scanner.getSubTypesOf(SparkRepository.class);


    }

    private void registerSparkBean(ConfigurableApplicationContext context) {
        String appName = context.getEnvironment().getProperty("spark.app-name");
        SparkSession sparkSession = SparkSession.builder().appName(appName).master("local[*]").getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        context.getBeanFactory().registerSingleton("sparkContext", sparkContext);
        context.getBeanFactory().registerSingleton("sparkSession", sparkSession);

    }
}
