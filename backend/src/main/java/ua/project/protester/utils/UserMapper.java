package ua.project.protester.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.exceptions.UserMapperException;
import ua.project.protester.model.User;
import ua.project.protester.model.UserDto;
import ua.project.protester.request.UserRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }





    public User toEntity(UserDto userDto)
    {
        return new User(userDto.getId(),userDto.getName(),userDto.getPassword(),userDto.getEmail(),userDto.isActive(),userDto.getFullName(),userDto.getRoles());
    }

    public UserDto toDto(User user)
    {

        return modelMapper.map(user,UserDto.class);
    }


    public UserDto toDtoFromRequest(UserRequest userRequest)
    {
        return Objects.isNull(userRequest)? null:modelMapper.map(userRequest,UserDto.class);

    }
}
