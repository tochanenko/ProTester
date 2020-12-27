package ua.project.protester.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.RoleNotFoundException;
import ua.project.protester.model.Role;
import ua.project.protester.model.User;
import ua.project.protester.repository.RoleRepository;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.response.RoleResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Optional<Role> findRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role with id " + id + "was not found!"));
        List<User> users = userRepository.findUsersByRoleId(role.getId());
        users.forEach(user -> user.getRole().setName(role.getName()));
        role.setUsers(users);
        return Optional.of(role);
    }

    @Transactional
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles
        ) {
            List<User> users = userRepository.findUsersByRoleId(role.getId());
            users.forEach(user -> user.getRole().setName(role.getName()));
            role.setUsers(users);
        }
        return roles;
    }

    @Transactional
    public Optional<Role> findRoleByName(String name) {
        Role role = roleRepository.findRoleByName(name).orElseThrow(() -> new RoleNotFoundException("Role with name " + name + "was`nt found!"));
        role.setUsers(userRepository.findUsersByRoleId(role.getId()));
        role.getUsers().forEach(user -> user.getRole().setName(role.getName()));
        return Optional.of(role);
    }

    public List<RoleResponse> getAllRoles() {
        return findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .collect(Collectors.toList());
    }

    public RoleResponse getRole(Long id) {
        return modelMapper.map(findRoleById(id), RoleResponse.class);
    }
}
