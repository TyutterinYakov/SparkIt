package ru.company.sparkdata.starter.handler;

import lombok.Builder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;
import ru.company.sparkdata.starter.extractor.DataExtractor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Builder
public class SparkInvocationHandler implements InvocationHandler {

    private Class<?> modelClass;
    private String pathToData;
    private DataExtractor dataExtractor;
    private Map<Method, List<SparkTransformation>> transformationChain;
    private Map<Method, Finalizer> finalizers;
    private ConfigurableApplicationContext context;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Dataset<Row> dataset = dataExtractor.load(pathToData, context);
        List<SparkTransformation> transformations = transformationChain.get(method);
        for (SparkTransformation transformation : transformations) {
            dataset = transformation.transform(dataset);
        }
        Finalizer finalizer = finalizers.get(method);
        return finalizer.doAction(dataset);
    }
}
