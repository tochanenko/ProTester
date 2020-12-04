package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.project.protester.model.executable.Step;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LibraryRequestModel {
    private Long id;

    private String name;

    private String description;

    private List<Step> components;
}
