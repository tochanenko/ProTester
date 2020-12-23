package ua.project.protester.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LightOuterComponentResponse {

    private Integer id;
    private boolean compound;
    private String name;
}
