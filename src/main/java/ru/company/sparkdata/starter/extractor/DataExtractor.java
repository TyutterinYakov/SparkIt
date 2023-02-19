package ru.company.sparkdata.starter.extractor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;

public interface DataExtractor {

    Dataset<Row> load(String pathToData, ConfigurableApplicationContext context);
}
