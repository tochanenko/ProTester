package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.model.executable.ExecutableComponent;
import ua.project.protester.model.executable.Step;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Library {

    private Integer id;

    private String name;

    private String description;

    private List<Step> components;

}


