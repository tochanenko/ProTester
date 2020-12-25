package ua.project.protester.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ua.project.protester.model.executable.result.subtype.SqlColumnDto;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
@Slf4j
public class SqlConverter {

    public static List<SqlColumnDto> convertResultSetToTable(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<SqlColumnDto> sqlTable = new ArrayList<>(metaData.getColumnCount());

            for (int i = 0; i < metaData.getColumnCount(); i++) {
                sqlTable.set(i, new SqlColumnDto(
                        metaData.getColumnName(i),
                        new LinkedList<>()));
            }

            while (resultSet.next()) {
                for (int i = 0; i < sqlTable.size(); i++) {
                    sqlTable.get(i).getRows().add(resultSet.getString(i + 1));
                }
            }

            return sqlTable;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}
