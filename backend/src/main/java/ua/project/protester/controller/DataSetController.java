package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.DataSet;
import ua.project.protester.response.DataSetResponse;
import ua.project.protester.service.DataSetService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

@Slf4j
@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DataSetController {

    private final DataSetService dataSetService;

    @GetMapping
    public Page<DataSetResponse> findAll(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                @RequestParam(value = "dataSetName", defaultValue = "") String dataSetName) {

        Pagination pagination = new Pagination(pageSize, pageNumber, dataSetName);

        System.out.println("dataSetName" + dataSetName);
        return dataSetService.findAllDataSets(pagination);
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
            return new ResponseEntity<>("DataSet wasn't found", HttpStatus.NOT_FOUND);
        }
        dataSetService.deleteDataSetById(id);
        return new ResponseEntity<>("DataSet was deleted", HttpStatus.OK);
    }

    @GetMapping("/name")
    public DataSetResponse findByName(@RequestParam String name) {
        return dataSetService.findDataSetByName(name);
    }

}
