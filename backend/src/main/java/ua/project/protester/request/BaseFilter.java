package ua.project.protester.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseFilter {

    private final Integer pageSize;
    private final Integer pageNumber;
    private final String filterName;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
