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
    private String searchField;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
