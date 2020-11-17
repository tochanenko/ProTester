package ua.project.protester.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.project.protester.db.MockDB;
import ua.project.protester.model.Role;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepository {

    private final MockDB mockDB;

    @Autowired
    public RoleRepository(MockDB mockDB) {

        this.mockDB = mockDB;
    }

    public Optional<Role> findRoleByName(String name) {
        return Optional.ofNullable(mockDB.findRoleByName(name));
    }

    public Optional<Role> findRoleById(Long id) {
        return Optional.ofNullable(mockDB.findRoleById(id));
    }

    public List<Role> findAllRoles() {
        return mockDB.findAllRoles();
    }
}
