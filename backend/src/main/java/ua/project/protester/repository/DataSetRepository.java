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
import ua.project.protester.model.DataSet;
import ua.project.protester.utils.Pagination;
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
        log.info("saving {} dataset with description {}", dataSet.getName(), dataSet.getDescription());

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveDataSet"),
                new MapSqlParameterSource()
                        .addValue("data_set_name", dataSet.getName())
                        .addValue("data_set_description", dataSet.getDescription()),
                keyHolder,
                new String[]{"data_set_id"});
        log.info("saving {} dataset with description {}", dataSet.getName(), dataSet.getDescription());
        Integer id = (Integer) keyHolder.getKey();
        dataSet.setId(id.longValue());
        dataSet.getDataset().forEach((key, value) -> saveParams(id.longValue(), key, value));
        return dataSet;
    }

    public DataSet updateDataSet(DataSet dataSet) {

        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateDataSet"),
                new MapSqlParameterSource()
                        .addValue("data_set_id", dataSet.getId())
                        .addValue("data_set_name", dataSet.getName())
                        .addValue("data_set_description", dataSet.getDescription()));
        log.info("updating {} dataset with description {} and id {}", dataSet.getName(), dataSet.getDescription(),dataSet.getId());

        deleteParamsById(dataSet.getId());
        dataSet.getDataset().forEach((key, value) -> saveParams(dataSet.getId(), key, value));

        return dataSet;
    }

    public void deleteDataSetById(Long id) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteDataSet"),
                new MapSqlParameterSource()
                        .addValue("data_set_id", id));
    }

    private Map<String, String> findParamsById(Long id) {
        Map<String, String> parameters = new HashMap<>();
        namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findParamsById"),
                new MapSqlParameterSource().addValue("data_set_id", id),
                (rs, rowNum) -> parameters.put(
                        rs.getString("data_set_key"),
                        rs.getString("data_set_value")));
        return parameters;
    }

    private void deleteParamsById(Long id) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteParamsById"),
                new MapSqlParameterSource().addValue("data_set_id", id));
    }

    private void saveParams(Long id, String key, String value) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "saveDataSetParameter"),
                new MapSqlParameterSource()
                        .addValue("data_set_id", id)
                        .addValue("data_set_key", key)
                        .addValue("data_set_value", value));
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
            dataSet.setDataset(findParamsById(dataSet.getId()));
            dataSet.setTestScenarios(findTestScenariosByDataSetId(dataSet.getId()));
             return Optional.of(dataSet);
        } catch (DataAccessException e) {
             log.warn("dataset with id {} was`nt found", id);
            return Optional.empty();
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
        } catch (EmptyResultDataAccessException e) {
            log.warn("dataset with name {} was`nt found", name);
            return Optional.empty();
        }
    }

    public List<DataSet> findAll() {
        try {
            List<DataSet> dataSet = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findAll"),
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

    public List<DataSet> findAll(Pagination pagination) {
        System.out.println("IN REPO  " + pagination.getSearchField());
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("pageSize", pagination.getPageSize());
        namedParams.addValue("offset", pagination.getOffset());
        namedParams.addValue("dataSetName", pagination.getSearchField() + "%");

        try {
            List<DataSet> dataSet = namedParameterJdbcTemplate.query(
                    PropertyExtractor.extract(env, "findAllPaginated"),
                    namedParams,
                    (rs, rowNum) -> new DataSet(
                            rs.getLong("data_set_id"),
                            rs.getString("data_set_name"),
                            rs.getString("data_set_description"))
            );
            if (dataSet.size() == 0) {
                return Collections.emptyList();
            }
            dataSet.forEach(dataSet1 -> dataSet1.setDataset(findParamsById(dataSet1.getId())));
            dataSet.forEach(dataSet1 -> dataSet1.setTestScenarios(findTestScenariosByDataSetId(dataSet1.getId())));
            return dataSet;
        } catch (EmptyResultDataAccessException e) {
            log.warn("datasets were`nt found");
            return Collections.emptyList();
        }
    }

    public Long count(Pagination pagination) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("dataSetName", pagination.getSearchField() + "%");

        return namedParameterJdbcTemplate.queryForObject(PropertyExtractor.extract(env, "countDataSet"),
                namedParams, Long.class);
    }

    public Optional<String> findValueByKeyAndId(Long id, String key) {
        return Optional.ofNullable(findParamsById(id).get(key));
    }


    private List<Long> findTestScenariosByDataSetId(Long id) {
        return namedParameterJdbcTemplate.queryForList(
                PropertyExtractor.extract(env, "findAllTestScenariosByDataSetId"),
                new MapSqlParameterSource()
                        .addValue("data_set_id", id), Long.class);
    }

    public List<DataSet> findDataSetByTestCaseId(Long id) {
        List<DataSet> dataSetList = namedParameterJdbcTemplate.query(
                PropertyExtractor.extract(env, "findAllDataSetByTestCase"),
                new MapSqlParameterSource()
                        .addValue("test_case_id", id),
                (rs, rowNum) -> new DataSet(
                        rs.getLong("data_set_id"),
                        rs.getString("data_set_name"),
                        rs.getString("data_set_description")));
        dataSetList.forEach(ds -> ds.setDataset(findParamsById(ds.getId())));
        dataSetList.forEach(ds -> ds.setTestScenarios(findTestScenariosByDataSetId(ds.getId())));
        return dataSetList;
    }
}

