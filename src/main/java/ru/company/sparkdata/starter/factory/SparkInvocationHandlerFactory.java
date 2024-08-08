package ru.company.sparkdata.starter.factory;

import ru.company.sparkdata.starter.SparkRepository;
import ru.company.sparkdata.starter.annotation.Source;
import ru.company.sparkdata.starter.extractor.DataExtractor;
import ru.company.sparkdata.starter.handler.SparkInvocationHandler;
import ru.company.sparkdata.starter.handler.SparkTransformation;
import ru.company.sparkdata.starter.resolver.DataExtractorResolver;
import ru.company.sparkdata.starter.transformation.TransformationSpider;
import ru.company.sparkdata.starter.util.WordsMather;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

public class SparkInvocationHandlerFactory {

    private final DataExtractorResolver dataExtractorResolver;
    private final Map<String, TransformationSpider> transformationSpiderMap;

    public SparkInvocationHandler create(Class<? extends SparkRepository> sparkRepoInterface) {
        final Class<?> modelClass = getModelClass(sparkRepoInterface);
        final String pathToData = modelClass.getAnnotation(Source.class).value();
        final Set<String> fieldNames = getFieldNames(modelClass);
        final DataExtractor dataExtractor = dataExtractorResolver.resolve(pathToData);
        final Map<Method, List<SparkTransformation>> transformationChain = new HashMap<>();

        for (Method method : sparkRepoInterface.getMethods()) {
            final String methodName = method.getName();
            final List<String> wordsFromMethodName = WordsMather.toWordJavaConvention(methodName);
            while (wordsFromMethodName.size() > 1) {
                final String spiderName = WordsMather.findAndRemoveMatchingPiecesIfExists(
                        transformationSpiderMap.keySet(),
                        wordsFromMethodName);

                if (!spiderName.isEmpty()) {
                    final TransformationSpider transformationSpider = transformationSpiderMap.get(spiderName);
                    transformationSpider.getTransformation(); //TODO 46:11
                }
            }


            SparkInvocationHandler.builder()
                    .modelClass(modelClass)
                    .pathToData(pathToData)
                    .dataExtractor(dataExtractor)
                    .transformationChain(transformationChain)
                    .context()
                    .finalizers()
                    .build();
        }

        private static Class<?> getModelClass (Class < ? extends SparkRepository > sparkRepoInterface){
            final ParameterizedType genericInterface = (ParameterizedType) sparkRepoInterface.getGenericInterfaces()[0];
            return (Class<?>) genericInterface.getActualTypeArguments()[0];
        }

        private static Set<String> getFieldNames (Class < ? > modelClass){
            return Arrays.stream(modelClass.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(Transient.class))
                    .filter(f -> Collection.class.isAssignableFrom(f.getType()))
                    .map(Field::getName)
                    .collect(Collectors.toSet());
        }

    }
