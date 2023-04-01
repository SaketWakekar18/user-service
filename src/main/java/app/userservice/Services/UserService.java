package app.userservice.Services;

import app.userservice.Entity.User;
import app.userservice.Payloads.UserResponse;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user, String userId);

    List<User> getAllUsers();

    User getUserByID(String userId);

    void deleteUser(String userId);

    User searchUserByEmail(String email);

    UserResponse getAllUsersWithPaginationAndSorting(int pageNumber, int pageSize, String sortBy);

}
