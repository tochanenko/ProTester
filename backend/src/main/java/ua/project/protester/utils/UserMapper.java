package ua.project.protester.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.model.User;
import ua.project.protester.model.UserDto;
import ua.project.protester.request.UserRequest;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getPassword(), userDto.getEmail(), userDto.isActive(), userDto.getFullName(), userDto.getRoles());
    }

    public UserDto toDtoFromRequest(UserRequest userRequest) {

        if (userRequest != null) {
            return modelMapper.map(userRequest, UserDto.class);
        }
        return null;
    }
}
