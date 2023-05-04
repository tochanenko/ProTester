package ua.project.protester.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseFilter {

    protected final Integer pageSize;
    protected final Integer pageNumber;
    protected final String filterName;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
