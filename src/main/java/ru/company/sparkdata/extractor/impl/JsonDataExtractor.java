package ru.company.sparkdata.extractor.impl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.company.sparkdata.extractor.DataExtractor;

public class JsonDataExtractor implements DataExtractor {
    @Override
    public Dataset<Row> load(String pathToData) {
        return null;
    }
}
