package ru.company.sparkdata.extractor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DataExtractor {

    Dataset<Row> load(String pathToData);
}
