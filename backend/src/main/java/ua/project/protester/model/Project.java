package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Long projectId;

    private String projectName;

    private String projectWebsiteLink;

    private Boolean projectActive;

    private Long creatorId;
}
