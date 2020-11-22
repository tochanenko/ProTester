package ua.project.protester.utils;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.project.protester.model.User;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toUserFromUserRequest(UserCreationRequestDto user) {
        if (user != null) {
            return modelMapper.map(user, User.class);
        }
        return null;
    }

    public UserResponse toUserRest(User user) {
        if (user != null) {
            return modelMapper.map(user, UserResponse.class);
        }
        return null;
    }

    public List<UserResponse> toUserRest(List<User> users) {
        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).collect(Collectors.toList());
    }

    public User toUserFromUserModificationDto(UserModificationDto user) {
        if (user != null) {
            return modelMapper.map(user, User.class);
        }
        return null;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, UserResponse.class)
                .addMappings(m -> m.skip(UserResponse::setRole)).setPostConverter(toRestConverter());
    }

    private Converter<User, UserResponse> toRestConverter() {
        return context -> {
            User source = context.getSource();
            UserResponse destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(User source, UserResponse destination) {
        destination.setRole(source.getRole().getName());
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }
}
