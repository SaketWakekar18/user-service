package app.userservice.Services.Implementation;

import app.userservice.Entity.User;
import app.userservice.Exceptions.EmailAlreadyExistsException;
import app.userservice.Exceptions.ResourceNotFoundException;
import app.userservice.Payloads.UserResponse;
import app.userservice.Repository.UserRepository;
import app.userservice.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        String userID = UUID.randomUUID().toString();
        user.setUserId(userID);
        if (!userRepository.existsByEmail(user.getEmail())) {
            User savedUser = this.userRepository.save(user);
            return savedUser;
        } else {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

    }

    @Override
    public User updateUser(User user, String userId) {
        User getUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server " + userId));
        getUser.setName(user.getName());
        getUser.setEmail(user.getEmail());
        getUser.setAbout(user.getAbout());
        User updatedUser = this.userRepository.save(getUser);
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = this.userRepository.findAll();
        return allUsers;
    }

    @Override
    public User getUserByID(String userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server " + userId));
    }

    @Override
    public void deleteUser(String userId) {
        User deletedUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server " + userId));
        this.userRepository.delete(deletedUser);
    }

    @Override
    public User searchUserByEmail(String email) {
        User searchUser = this.userRepository.findByEmail(email);
        return searchUser;
    }

    @Override
    public UserResponse getAllUsersWithPaginationAndSorting(int pageNumber, int pageSize, String sortBy) {
        Page<User> users = this.userRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));
        List<User> userList = users.stream().toList();
        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userList);
        userResponse.setPageNumber(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLastPage(users.isLast());
        return userResponse;
    }
}
