package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.exception.executable.ExecutableComponentNotFoundException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.model.executable.ExecutableComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.utils.LibraryRowMapper;
import ua.project.protester.utils.PropertyExtractor;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@PropertySource("classpath:queries/library.properties")
public class LibraryRepositoryImpl implements LibraryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Environment env;
    private final LibraryRowMapper rowMapper;
    private final ActionRepository actionRepository;
    private final OuterComponentRepository outerComponentRepository;
    private final StepParameterRepository stepParameterRepository;

    @Override
    public void createLibrary(Library library) {
        String sql = PropertyExtractor.extract(env, "createLibrary");
        String idColumnName = "library_id";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                sql,
                new BeanPropertySqlParameterSource(library),
                keyHolder,
                new String[]{idColumnName});
        Integer libraryId = (Integer) keyHolder.getKey();

        saveLibraryStorages(library, libraryId);
    }

    @Override
    public void updateLibrary(Library library, int id) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "updateLibrary"),
                new MapSqlParameterSource()
                        .addValue("library_id", id)
                        .addValue("library_name", library.getName())
                        .addValue("library_description", library.getDescription())
        );
        deleteLibrariesStorage(id);
        saveLibraryStorages(library, id);
    }

    @Override
    public List<Library> getList(int count, int offset) {
        String sql = PropertyExtractor.extract(env, "getList");
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("count", count);
        namedParams.addValue("offset", offset);
        List<Library> allLibraries = namedParameterJdbcTemplate.query(
                sql,
                namedParams,
                new BeanPropertyRowMapper<>(Library.class)
        );

        allLibraries
                .forEach(library -> {
                    library.setComponents(
                            findAllLibraryStorageById(library.getId()));
                });

        return allLibraries;
    }

    @Override
    public Optional<Library> findLibraryById(Integer id) throws LibraryNotFoundException {
        String sql = PropertyExtractor.extract(env, "findLibraryById");

        try {
            Library library = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource().addValue("library_id", id),
                    new BeanPropertyRowMapper<>(Library.class)
            );
            if (library == null) {
                return Optional.empty();
            }

            List<Step> steps = findAllLibraryStorageById(library.getId());
            library.setComponents(steps);
            return Optional.of(library);

        } catch (DataAccessException e) {
            throw new LibraryNotFoundException();
        }
    }

    @Override
    public Optional<Library> findByName(String name) {
        try {
            String sql = PropertyExtractor.extract(env, "findLibraryByName");
            MapSqlParameterSource namedParams = new MapSqlParameterSource();

            namedParams.addValue("library_name", name);

            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, namedParams, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteLibraryById(Integer id) {
        String sql = PropertyExtractor.extract(env, "deleteLibraryById");

        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource().addValue("library_id", id)
        );
    }

    private void deleteLibrariesStorage(Integer id) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "deleteLibraryStorageByLibraryId"),
                new MapSqlParameterSource().addValue("library_id", id)
        );
    }

    private List<Step> findAllLibraryStorageById(Integer id) {
        String sql = PropertyExtractor.extract(env, "findAllLibrariesStorage");

        return namedParameterJdbcTemplate.query(
                sql,
                new MapSqlParameterSource().addValue("library_id", id),
                (rs, rowNum) -> initLibraryStorage(
                        rs.getInt("library_storage_id"),
                        rs.getBoolean("is_action"),
                        rs.getInt("action_id"),
                        rs.getInt("compound_id")
                ));

    }

    private Step initLibraryStorage(int id, boolean isAction, int actionId, int compoundId) {
        try {
            ExecutableComponent component =
                    isAction
                            ?
                            actionRepository.findActionById(actionId).orElseThrow(ExecutableComponentNotFoundException::new)
                            :
                            outerComponentRepository.findOuterComponentById(compoundId, true).orElseThrow(ExecutableComponentNotFoundException::new);

            Map<String, String> parameters = stepParameterRepository.findAllDataSetParamsId(id);
            return new Step(id, isAction, component, parameters);
        } catch (OuterComponentNotFoundException e) {
            throw new ExecutableComponentNotFoundException();
        }
    }

    private void saveLibraryStorages(Library library, Integer libraryId) {
        ListIterator<Step> libraryStepsIterator = library.getComponents().listIterator();
        Step libraryStep;

        while (libraryStepsIterator.hasNext()) {
            libraryStep = libraryStepsIterator.next();
            saveLibraryStorage(libraryId, libraryStep);
        }
    }

    private void saveLibraryStorage(Integer libraryId, Step step) {
        namedParameterJdbcTemplate.update(
                PropertyExtractor.extract(env, "addComponent"),
                new MapSqlParameterSource()
                        .addValue("is_action", step.isAction())
                        .addValue("library_id", libraryId)
                        .addValue("action_id", step.isAction() ? step.getId() : null)
                        .addValue("compound_id", step.isAction() ? null : step.getId())
        );
    }
}
