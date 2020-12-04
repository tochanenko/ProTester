package ua.project.protester.utils;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.Library;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LibraryRowMapper implements RowMapper<Library> {

    @Override
    public Library mapRow(ResultSet resultSet, int i) throws SQLException {
        Library library = new Library();

        library.setId(resultSet.getInt("library_id"));
        library.setName(resultSet.getString("library_name"));
        library.setDescription(resultSet.getString("library_description"));

        return library;
    }
}
