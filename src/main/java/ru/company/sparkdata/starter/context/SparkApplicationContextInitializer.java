package ru.company.sparkdata.starter.context;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import ru.company.sparkdata.starter.SparkRepository;

import java.beans.Introspector;
import java.lang.reflect.Proxy;
import java.util.Set;

public class SparkApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        registerSparkBean(context);
        final Reflections reflections = new Reflections();
        final Set<Class<? extends SparkRepository>> repositories = reflections.getSubTypesOf(SparkRepository.class);

        repositories.forEach(in -> {
            final Object golem = Proxy.newProxyInstance(in.getClassLoader(), new Class[]{in}, ih);
            context.getBeanFactory().registerSingleton(Introspector.decapitalize(in.getSimpleName()), golem);
        });

    }

    private void registerSparkBean(ConfigurableApplicationContext context) {
        final String appName = context.getEnvironment().getProperty("spark.app-name");
        final SparkSession sparkSession = SparkSession.builder().appName(appName).master("local[*]").getOrCreate();
        final JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton("sparkContext", sparkContext);
        beanFactory.registerSingleton("sparkSession", sparkSession);

    }
}
