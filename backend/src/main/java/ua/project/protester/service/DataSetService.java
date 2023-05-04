package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.DataSetNotFoundException;
import ua.project.protester.model.DataSet;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.response.DataSetResponse;
import ua.project.protester.utils.DataSetMapper;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataSetService {
    private final DataSetRepository dataSetRepository;
    private final DataSetMapper dataSetMapper;

    @Transactional
    public DataSetResponse saveDataSet(DataSet dataSet) {
        return dataSetMapper.toDataSetResponseFromDataSet(
                dataSetRepository.saveDataSet(dataSet));
    }

    @Transactional
    public DataSetResponse updateDataSet(DataSet dataSet) {

        return dataSetMapper.toDataSetResponseFromDataSet(
                dataSetRepository.updateDataSet(dataSet));
    }

    @Transactional
    public void deleteDataSetById(Long id) {
        dataSetRepository.deleteDataSetById(id);
    }

    @Transactional
    public DataSetResponse findDataSetById(Long id) {
        return dataSetMapper.toDataSetResponseFromDataSet(
                dataSetRepository.findDataSetById(id)
                .orElseThrow(() -> new DataSetNotFoundException("DataSet was`nt found!")));
    }

    @Transactional
    public DataSetResponse findDataSetByName(String name) {
        return dataSetMapper.toDataSetResponseFromDataSet(
                dataSetRepository.findDataSetByName(name)
                        .orElseThrow(() -> new DataSetNotFoundException("DataSet was`nt found!")));
    }

    @Transactional
    public List<DataSetResponse> findAll() {
        return dataSetRepository.findAll()
                .stream()
                .map(dataSetMapper::toDataSetResponseFromDataSet)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<DataSetResponse> findAllDataSets(Pagination pagination) {
        log.info("IN findAllDataSet");
        System.out.println(pagination.getSearchField());
        return new Page<>(
                dataSetRepository.findAll(pagination)
                        .stream()
                        .map(dataSetMapper::toDataSetResponseFromDataSet)
                .collect(Collectors.toList()),
                dataSetRepository.count(pagination)
        );
    }
}
