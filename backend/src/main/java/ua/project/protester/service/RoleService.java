package ua.project.protester.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.model.Role;
import ua.project.protester.repository.RoleRepository;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.response.RoleResponse;

import java.util.List;
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
    public Role findRoleById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role != null) {
            role.setUsers(userRepository.findUsersByRoleId(role.getId()));
            return role;
        }
        return null;
    }

    @Transactional
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        roles.forEach(role -> role.setUsers(userRepository.findUsersByRoleId(role.getId())));
        return roles;
    }

    @Transactional
    public Role findRoleByName(String name) {
        Role role = roleRepository.findRoleByName(name).orElse(null);

        if (role != null) {
            role.setUsers(userRepository.findUsersByRoleId(role.getId()));
            return role;
        }

        return null;
    }

    public List<RoleResponse> getAllRoles() {
        return findAll().stream().map(role -> modelMapper.map(role, RoleResponse.class)).collect(Collectors.toList());
    }

    public RoleResponse getRole(Long id) {
        return modelMapper.map(findRoleById(id), RoleResponse.class);
    }
}
