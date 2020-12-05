package ua.project.protester.request;

import lombok.Getter;
import lombok.ToString;
import ua.project.protester.model.executable.ExecutableComponentType;

@Getter
@ToString
public class ActionFilter extends BaseFilter {

    private final ExecutableComponentType type;

    public ActionFilter(Integer pageSize, Integer pageNumber, String filterName, ExecutableComponentType type) {
        super(pageSize, pageNumber, filterName);
        this.type = type;
    }

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
