package ua.project.protester.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean projectActive;
    private String projectName;

    public Pagination(Integer pageSize, Integer pageNumber, String projectName) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.projectName = projectName;
    }

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    public boolean isFilterPresent() {
        return projectActive != null;
    }
}
