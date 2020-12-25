package ua.project.protester.utils.testcase;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.TestCase;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TestCaseRowMapper implements RowMapper<TestCase> {
    @Override
    public TestCase mapRow(ResultSet resultSet, int i) throws SQLException {
        TestCase testCase = new TestCase();

        testCase.setId(resultSet.getLong("test_case_id"));
        testCase.setName(resultSet.getString("test_case_name"));
        testCase.setDescription(resultSet.getString("test_case_description"));
        testCase.setProjectId(resultSet.getLong("project_id"));
        testCase.setAuthorId(resultSet.getLong("author_id"));
        testCase.setScenarioId(resultSet.getLong("scenario_id"));
        testCase.setDataSetId(resultSet.getLong("data_set_id"));

        return testCase;
    }
}
