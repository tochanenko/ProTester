package ua.project.protester.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ua.project.protester.model.Role;
import ua.project.protester.model.Roles;
import ua.project.protester.model.User;
import ua.project.protester.utils.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockDB {

    private static Role adminRole=new Role(1L,Roles.ROLE_ADMIN.name());
    private static Role managerRole=new Role(2L,Roles.ROLE_MANAGER.name());
    private static Role engineerRole=new Role(3L,Roles.ROLE_ENGINEER.name());
    private static User admin=new User(1L,"admin","admin","admin@gmail.com",true,"Admin Adminenko",adminRole);
    private static User manager=new User(2L,"manager","manager","manager@gmail.com",true,"Manager Managerenko",managerRole);
    private static User engineer=new User(3L,"engineer","engineer","engineer@gmail.com",true,"Engineer Engineerenko",engineerRole);

    private  static List<Role>ROLES=new ArrayList<>();

    private static List<User>USERS=new ArrayList<>();

    static {
        List<User>admins=new ArrayList<>();
        admins.add(admin);
        adminRole.setUsers(admins);

        List<User>managers=new ArrayList<>();
        managers.add(manager);
        managerRole.setUsers(managers);

        List<User>engineers=new ArrayList<>();
        engineers.add(engineer);
        engineerRole.setUsers(engineers);

        ROLES.add(adminRole);
        ROLES.add(managerRole);
        ROLES.add(engineerRole);

        USERS.add(admin);
        USERS.add(manager);
        USERS.add(engineer);
    }

    public User findUserById(Long id){
        return USERS.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }

    public User findUserByEmail(String email){
        return USERS.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    public Role findRoleByName(String name){
        return ROLES.stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }

    public Role findRoleById(Long id){
        return ROLES.stream().filter(role -> role.getId().equals(id)).findFirst().orElse(null);
    }

    public ResponseEntity<String> addUser(User user){
        String usr=user.getRole().getName();
        Role role=findRoleByName(usr);
        User userFromDB=findUserByEmail(user.getEmail());
        if(userFromDB!=null){
            return new ResponseEntity<>("User already exists!", HttpStatus.BAD_REQUEST);
        }
        if(role!=null){
            role.getUsers().add(user);
            user.setRole(role);
        }else {
            Role newRole = new Role((long) (ROLES.size() + 1),user.getRole().getName(),List.of(user));
            user.setRole(null);
            user.setRole(newRole);
            ROLES.add(newRole);
        }
        user.setId((long)USERS.size()+1);
        USERS.add(user);
        return new ResponseEntity<>("User was successfully created!",HttpStatus.OK);
    }

}
