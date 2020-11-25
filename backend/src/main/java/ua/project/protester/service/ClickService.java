package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.protester.actions.ClickAction;
import ua.project.protester.repository.ActionRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClickService {
    private final ActionRepository actionRepository;
    private final ClickAction clickAction;
    public void click(Map<String,String> params) {
        clickAction.invoke(params);
    }
}
