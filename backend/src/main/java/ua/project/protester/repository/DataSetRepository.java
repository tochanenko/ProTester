package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.exception.DataSetNotFoundException;
import ua.project.protester.model.DataSet;
import ua.project.protester.response.DataSetResponse;
import ua.project.protester.utils.PropertyExtractor;

import java.util.*;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/dataset.properties")
@Slf4j
public class DataSetRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;

    public DataSet saveDataSet(DataSet dataSet) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveDataSet"),
                new MapSqlParameterSource()
                        .addValue("data_set_name", dataSet.getName())
                        .addValue("data_set_description", dataSet.getDescription()),
                keyHolder,
                new String[]{"data_set_id"});
        log.info("saving {} dataset with description {}",dataSet.getName(), dataSet.getDescription());
        Long id = ((Long)keyHolder.getKey());
        dataSet.setId(id);
        dataSet.getDataset().forEach( (key, value) -> saveParams(id, key, value));
        return dataSet;
    }

    public void updateDataSet(DataSet dataSet) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateDataSet"),
                new MapSqlParameterSource()
                        .addValue("data_set_id", dataSet.getId())
                        .addValue("data_set_name", dataSet.getName())
                        .addValue("data_set_description", dataSet.getDescription()),
                keyHolder,
                new String[]{"data_set_id"});
        log.info("updating {} dataset with description {}",dataSet.getName(), dataSet.getDescription());

        dataSet.getDataset().forEach( (key, value) -> saveParams(dataSet.getId(), key, value));
    }

    private Map<String, String> findParamsById(Long id) {
        Map<String, String> parameters = new HashMap<>();
        namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findParamsById"),
                new MapSqlParameterSource().addValue("data_set_id", id),
                (rs, rowNum) -> parameters.put(
                        rs.getString("key"),
                        rs.getString("value")));
        return parameters;
    }

    private void saveParams(Long id, String key, String value) {
            namedParameterJdbcTemplate.update(
                    PropertyExtractor.extract(env, "saveDataSetParameter"),
                    new MapSqlParameterSource()
                            .addValue("data_set_id", id)
                            .addValue("key", key)
                            .addValue("value", value));
        }

    public Optional<DataSet> findDataSetById(Long id) {
         try {
            DataSet dataSet = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findDataSetById"),
                    new MapSqlParameterSource().addValue("data_set_id", id),
                    (rs, rowNum) -> new DataSet(
                            rs.getLong("data_set_id"),
                            rs.getString("data_set_name"),
                            rs.getString("data_set_description"))
            );
            if (dataSet == null) {
                return Optional.empty();
            }
             dataSet.setDataset(findParamsById(id));
             return Optional.of(dataSet);
        } catch (DataAccessException e) {
             log.warn("dataset with id {} was`nt found",id);
            throw new DataSetNotFoundException("Data set was`nt found!");
        }
    }

    public Optional<DataSet> findDataSetByName(String name) {
        try {
            DataSet dataSet = namedParameterJdbcTemplate.queryForObject(
                    PropertyExtractor.extract(env, "findDataSetByName"),
                    new MapSqlParameterSource().addValue("data_set_name", name),
                    (rs, rowNum) -> new DataSet(
                            rs.getLong("data_set_id"),
                            rs.getString("data_set_name"),
                            rs.getString("data_set_description"))
            );
            if (dataSet == null) {
                return Optional.empty();
            }
            dataSet.setDataset(findParamsById(dataSet.getId()));
            return Optional.of(dataSet);
        } catch (DataAccessException e) {
            log.warn("dataset with name {} was`nt found",name);
            throw new DataSetNotFoundException("Data set was`nt found!");
        }
    }

    public List<DataSet> findAll() {
        try {
            List<DataSet> dataSet = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findDataSetByName"),
                    (rs, rowNum) -> new DataSet(
                            rs.getLong("data_set_id"),
                            rs.getString("data_set_name"),
                            rs.getString("data_set_description"))
            );
            if (dataSet.size() == 0) {
                return Collections.emptyList();
            }
            dataSet.forEach(dataSet1 -> dataSet1.setDataset(findParamsById(dataSet1.getId())));
            return dataSet;
        } catch (EmptyResultDataAccessException e) {
            log.warn("datasets were`nt found");
            return Collections.emptyList();
        }
    }

    public Optional<String> findValueByKeyAndId(Long id, String key) {
       return Optional.ofNullable(findParamsById(id).get(key));
    }
}

