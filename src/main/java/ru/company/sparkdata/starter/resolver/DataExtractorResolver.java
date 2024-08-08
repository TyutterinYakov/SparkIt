package ru.company.sparkdata.starter.resolver;

import ru.company.sparkdata.starter.extractor.DataExtractor;

import java.util.Map;

public class DataExtractorResolver {

    private final Map<String, DataExtractor> dataExtractorMap;

    public DataExtractor resolve(String pathToData) {
        final String[] split = pathToData.split("\\.");
        final String fileExtension = split[split.length - 1];
        return dataExtractorMap.getOrDefault(fileExtension, (f, d) -> {
            throw new RuntimeException("Типа DataExtractor " + fileExtension + " еще нет!!");
        });
    }
}
