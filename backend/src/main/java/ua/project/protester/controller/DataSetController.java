package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.DataSet;
import ua.project.protester.response.DataSetResponse;
import ua.project.protester.service.DataSetService;

import java.util.List;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DataSetController {

    private final DataSetService dataSetService;

    @GetMapping
    public List<DataSetResponse> findAll() {
        return dataSetService.findAll();
    }

    @PostMapping
    public DataSetResponse save(@RequestBody DataSet dataSet) {
        return dataSetService.saveDataSet(dataSet);
    }
}
