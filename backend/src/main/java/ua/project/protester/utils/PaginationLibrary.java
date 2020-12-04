package ua.project.protester.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationLibrary {
    private Integer pageSize;
    private Integer pageNumber;
    private String name;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
