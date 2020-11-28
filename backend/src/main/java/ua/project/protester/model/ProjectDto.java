package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long projectId;

    private String projectName;

    private String projectWebsiteLink;

    private Boolean projectActive;

    private String creatorUsername;

    private Long creatorId;
}

