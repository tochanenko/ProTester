package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.executable.Step;
import ua.project.protester.utils.PropertyExtractor;

import java.util.ListIterator;

@Repository
@RequiredArgsConstructor
public class DataSetRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public void saveCompound(DataSet dataSet) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveCompound"),
                new BeanPropertySqlParameterSource(dataSet),
                keyHolder,
                new String[]{"dataset_id"});
        Integer compoundId = (Integer) keyHolder.getKey();

    }
}
