package ua.project.protester.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.project.protester.db.MockDB;
import ua.project.protester.model.Role;

import java.util.Optional;

@Repository
public class RoleRepository {
    private final MockDB mockDB;

    @Autowired
    public RoleRepository(MockDB mockDB) {
        this.mockDB = mockDB;
    }

    public Optional<Role> findRoleByRoleName(String name) {

        return Optional.ofNullable(mockDB.findRoleByName(name));
    }

    public Optional<Role> findRoleByRoleName(Long id) {

        return Optional.ofNullable(mockDB.findRoleById(id));
    }
}
