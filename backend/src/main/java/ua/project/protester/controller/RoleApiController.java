package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.response.RoleResponse;
import ua.project.protester.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleApiController {

    private RoleService roleService;

    @Autowired
    public RoleApiController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path = "/roles")
    public List<RoleResponse> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/role/{id}")
    public RoleResponse getRole(@PathVariable(name = "id") Long id) {
        return roleService.getRole(id);
    }

}
