package ru.company.sparkdata.starter.factory;

import ru.company.sparkdata.starter.SparkRepository;
import ru.company.sparkdata.starter.annotation.Source;
import ru.company.sparkdata.starter.extractor.DataExtractor;
import ru.company.sparkdata.starter.handler.SparkInvocationHandler;
import ru.company.sparkdata.starter.resolver.DataExtractorResolver;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SparkInvocationHandlerFactory {

    private final DataExtractorResolver dataExtractorResolver;

    public SparkInvocationHandler create(Class<? extends SparkRepository> sparkRepoInterface) {
        final Class<?> modelClass = getModelClass(sparkRepoInterface);
        final String pathToData = modelClass.getAnnotation(Source.class).value();
        final Set<String> fieldNames = getFieldNames(modelClass);
        final DataExtractor dataExtractor = dataExtractorResolver.resolve(pathToData);

        SparkInvocationHandler.builder()
                .modelClass(modelClass)
                .pathToData(pathToData)
                .dataExtractor(dataExtractor)
                .transformationChain()
                .context()
                .finalizers()
                .build();
    }





    private Class<?> getModelClass(Class<? extends SparkRepository> sparkRepoInterface) {
        final ParameterizedType genericInterface = (ParameterizedType) sparkRepoInterface.getGenericInterfaces()[0];
        return (Class<?>) genericInterface.getActualTypeArguments()[0];
    }

    private Set<String> getFieldNames(Class<?> modelClass) {
        return Arrays.stream(modelClass.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Transient.class))
                .filter(f -> Collection.class.isAssignableFrom(f.getType()))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

}
