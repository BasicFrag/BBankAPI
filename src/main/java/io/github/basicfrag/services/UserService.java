package io.github.basicfrag.services;


import io.github.basicfrag.exceptions.InternalServerLogicException;
import io.github.basicfrag.exceptions.ResourceNotFoundException;
import io.github.basicfrag.persistence.dao.UserDao;
import io.github.basicfrag.persistence.dto.UserDto;
import io.github.basicfrag.persistence.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.UriInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserDao userDao;

    @Inject
    UriInfo uriInfo;

    @Inject
    ServiceUtils utils;


    public UserDto getUserById(Long id) {
        Optional<User> userOptional = this.userDao.findEntityById(id);
        Map<String, String> error = this.utils.userNotFoundMessage();

        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException(error));

        return this.utils.userToDto(user);
    }

    public List<User> getAll() throws ResourceNotFoundException {
        Optional<List<User>> allUsers = this.userDao.findAllEntities();
        Map<String, String> error = this.utils.userNotFoundMessage();

        return allUsers.orElseThrow(() -> new ResourceNotFoundException(error));
    }

    public Map<String, String> createUser(UserDto data) {
        if (this.utils.isUserMandatoryInfoValidated(data)) {
            User user = this.utils.dtoToUser(data);
            this.userDao.persistEntity(user);
        }

        Map<String, String> message = new LinkedHashMap<>();
        String usersResource = uriInfo.getBaseUri() + "api/v1/users";
        String createAccountResource = uriInfo.getBaseUri() + "api/v1/accounts";
        String updateUserResource = uriInfo.getBaseUri() + "api/v1/users/{resourceId}";
        String deleteUserResource = uriInfo.getBaseUri() + "api/v1/users/{resourceId}";

        message.put("message", "Success upon persisting resource!");
        message.put("usersList", usersResource);
        message.put("createAccount", createAccountResource);
        message.put("updateUser", updateUserResource);
        message.put("deleteUser", deleteUserResource);

        return message;
    }

    public Map<String, String> updateUser(Long id, UserDto userDto) {
        Optional<User> userOptional = this.userDao.findEntityById(id);
        Map<String, String> message = new LinkedHashMap<>();
        String usersListResource = uriInfo.getBaseUri() + "api/v1/users";
        String updatedUserResource = uriInfo.getBaseUri() + "api/v1/users/{resourceId}";
        String deleteUserResource = uriInfo.getBaseUri() + "api/v1/users/{resourceId}";

        User oldUser = userOptional
                .orElseThrow(() -> new ResourceNotFoundException(this.utils.userNotFoundMessage()));
        this.utils.isUserUpdateInfoValidated(userDto);

        oldUser.setAddress(userDto.getAddress());
        oldUser.setTelNumber(userDto.getTelNumber());

        this.userDao.updateEntity(oldUser);

        if (this.utils.isUserUpdated(id, oldUser)) {
            message.put("message", "Success upon updating resource!");
            message.put("updatedUser", updatedUserResource);
            message.put("usersList", usersListResource);
            message.put("deleteUser", deleteUserResource);
        } else {
            message = this.utils.internalServerErrorMessage();
            throw new InternalServerLogicException(message);
        }
        return message;
    }

    public Map<String, String> removeUser(Long id) throws InternalServerLogicException {
        Map<String, String> message = new LinkedHashMap<>();
        String usersListResource = uriInfo.getBaseUri() + "api/v1/users";
        String createUserResource = uriInfo.getBaseUri() + "api/v1/users";
        this.userDao.findEntityById(id)
                .orElseThrow(() -> new ResourceNotFoundException(this.utils.userNotFoundMessage()));
        this.userDao.removeEntity(id);

        message.put("message", "Success upon deleting resource!");
        message.put("usersList", usersListResource);
        message.put("createUser", createUserResource);


        return message;
    }

}
