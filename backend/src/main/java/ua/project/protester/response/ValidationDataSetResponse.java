package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationDataSetResponse {

    private String dataSetName;

    private ValidationDataSetStatus status;

    private List<String> missingParameters;
}
