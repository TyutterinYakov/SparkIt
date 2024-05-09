package ru.company.sparkdata.starter;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spark")
@Setter
@Getter
@Component
public class SparkPropsHolder {
    private String appName;
    private String packagesToScan;
}
