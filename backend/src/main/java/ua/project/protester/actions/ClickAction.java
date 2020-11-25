package ua.project.protester.actions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ua.project.protester.annotation.Action;
import ua.project.protester.model.BaseAction;

import java.util.Map;

@Action(name = "Click action",description = "Click mouse action")
@Component
@Getter
@Setter
public class ClickAction extends BaseAction {

    @Override
    public void invoke(Map<String,String> map) {
        System.out.println("This was a click action!");
    }

}
