package app.userservice.Services;

import app.userservice.Entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user, String userId);

    List<User> getAllUsers();

    User getUserByID(String userId);

    void deleteUser(String userId);
}
