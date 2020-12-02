package ua.project.protester.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.model.DataSet;
import ua.project.protester.response.DataSetResponse;

@Component
public class DataSetMapper {

    private ModelMapper modelMapper;

    @Autowired
    public DataSetMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DataSetResponse toDataSetResponseFromDataSet(DataSet dataSet) {
        if (dataSet == null) {
            throw new RuntimeException();
        }
        return modelMapper.map(dataSet, DataSetResponse.class);
    }

}
