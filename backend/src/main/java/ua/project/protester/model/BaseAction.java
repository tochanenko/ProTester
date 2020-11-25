package ua.project.protester.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class BaseAction {

    protected Integer id;
    protected Integer typeId;
    protected String name;
    protected String description;

    protected Map<String, String> parameters = new HashMap<>();

    public void init(Integer typeId, String name, String description) {
        this.id = null;
        this.typeId = typeId;
        this.name = name;
        this.description = description;
    }

    public boolean hasSameSignature(BaseAction that) {
        return this.typeId.equals(that.typeId) && this.name.equals(that.name);
    }

    public void invoke(Map<String, String> params) {
    }
}
