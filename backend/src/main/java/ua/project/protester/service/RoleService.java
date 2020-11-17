package ua.project.protester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.protester.model.Role;
import ua.project.protester.repository.RoleRepository;
import ua.project.protester.response.RoleResponse;
import ua.project.protester.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public RoleService(UserMapper userMapper, RoleRepository roleRepository, UserService userService) {
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name).orElse(null);
    }

    public Role findRoleById(Long id) {
        return roleRepository.findRoleById(id).orElse(null);
    }

    public List<RoleResponse> findAllRoles() {

        List<RoleResponse> roleResponses = roleRepository.findAllRoles().stream().map(role -> userMapper.getModelMapper().map(role, RoleResponse.class)).collect(Collectors.toList());

        roleResponses.forEach(roleResponse -> roleResponse.setUsers(userMapper.toUserRest(userService.findUserByRoleId(roleResponse.getId()))));
        return roleResponses;
    }
}
