package ru.company.sparkdata.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.company.sparkdata.starter.annotation.Source;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/speakers.json")
public class Speaker {
    private String name;
    private int age;

}
