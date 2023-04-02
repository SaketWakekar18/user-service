package app.userservice.Services;

import app.userservice.Entity.User;
import app.userservice.Payloads.UserResponse;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user, int userId);

    List<User> getAllUsers();

    User getUserByID(int userId);

    void deleteUser(int userId);

    User searchUserByEmail(String email);

    UserResponse getAllUsersWithPaginationAndSorting(int pageNumber, int pageSize, String sortBy);

}
