package ru.company.sparkdata.starter.handler;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface Finalizer {
    Object doAction(Dataset<Row> dataset);
}
