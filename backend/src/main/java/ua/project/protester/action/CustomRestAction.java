package ua.project.protester.action;

import okhttp3.*;
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
        name = "Send ${method} request to url ${url} with body ${body}",
        type = ExecutableComponentType.REST,
        description = "Send request with the specified method to the specified url with the specified body",
        parameterNames = {"method", "url", "body"}
)
public class CustomRestAction extends AbstractAction {

    @Override
    protected ActionResultRestDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, Environment environment, OkHttpClient httpClient) {
        try {
            String requestBody = params.get("body");

            Request request = new Request.Builder()
                    .url(params.get("url"))
                    .method(
                            params.get("method"),
                            RequestBody.create(
                                    MediaType.parse("application/json"),
                                    requestBody))
                    .build();

            try {
                Response response = httpClient.newCall(request).execute();
                ResponseBody body = response.body();
                return new ActionResultRestDto(
                        requestBody,
                        body != null ? body.string() : "",
                        response.code());
            } catch (IOException e) {
                return new ActionResultRestDto(
                        new ActionExecutionException(e.getMessage()),
                        requestBody,
                        "",
                        0);
            }
        } catch (Exception e) {
            return new ActionResultRestDto(
                    new ActionExecutionException(e.getMessage()),
                    params.get("body"),
                    "",
                    0);
        }
    }
}
