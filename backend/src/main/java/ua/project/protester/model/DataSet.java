package ua.project.protester.model;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class DataSet {

    private Long id;

    private String name;

    private String description;

    private Map<String, String> dataset;
}
