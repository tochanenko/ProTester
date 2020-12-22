package ua.project.protester.action;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.openqa.selenium.WebDriver;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultRestDto;

import java.io.IOException;
import java.util.Map;

@Action(
        name = "Send get request to url ${url}",
        type = ExecutableComponentType.REST,
        description = "Send get request to the specified url",
        parameterNames = {"url"}
)
public class GetRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, OkHttpClient httpClient) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(params.get("url"))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                return new ActionResultRestDto(
                        "",
                        body != null ? body.string() : "",
                        response.code());
            } catch (IOException e) {
                return new ActionResultRestDto(
                        new ActionExecutionException(e.getMessage()),
                        "",
                        "",
                        0);
            }
        } catch (Exception e) {
            return new ActionResultRestDto(
                    new ActionExecutionException(e.getMessage()),
                    "",
                    "",
                    0);
        }
    }
}
