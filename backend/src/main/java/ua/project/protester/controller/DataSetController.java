package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping
    public DataSetResponse update(@RequestBody DataSet dataSet) {
        return dataSetService.updateDataSet(dataSet);
    }

    @GetMapping("/{id}")
    public DataSetResponse findById(@PathVariable Long id) {
        return dataSetService.findDataSetById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDataSet(@PathVariable Long id) {
        if (dataSetService.findDataSetById(id) == null) {
            return new ResponseEntity<>("DataSet was`nt found", HttpStatus.NOT_FOUND);
        }
        dataSetService.deleteDataSetById(id);
        return new ResponseEntity<>("DataSet was deleted", HttpStatus.OK);
    }

    @GetMapping("/name")
    public DataSetResponse findByName(@RequestParam String name) {
        return dataSetService.findDataSetByName(name);
    }

}
