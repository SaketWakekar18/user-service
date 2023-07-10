package app.userservice.Controller;

import app.userservice.Entity.User;
import app.userservice.Payloads.APIResponse;
import app.userservice.Payloads.AppConstants;
import app.userservice.Payloads.UserResponse;
import app.userservice.Services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    int retryCount = 1;

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user, @PathVariable int userId) {
        User updatedUser = this.userService.updateUser(user, userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = this.userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    //@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    @GetMapping("/getUserByID/{userId}")
    public ResponseEntity<User> getUserByID(@PathVariable int userId) {
        System.out.println("Retry Count :" + retryCount);
        retryCount++;
        User userByID = this.userService.getUserByID(userId);
        return ResponseEntity.ok(userByID);
    }

    public ResponseEntity<User> ratingHotelFallback(int userId, Exception ex) {
        ex.printStackTrace();
        System.out.println("This service is down: " + ex.getMessage());
        User user = User.builder()
                .userId(10)
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This is a dummy entry")
                .build();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable int userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity(new APIResponse("User deleted Successfully!!", true, HttpStatus.GONE), HttpStatus.OK);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<User> searchUser(@RequestParam String email) {
        User searchUser = this.userService.searchUserByEmail(email);
        return ResponseEntity.ok(searchUser);
    }

    @GetMapping("/pagination")
    public ResponseEntity<UserResponse> getAllPostsByUserWithPaginationAndSorting(@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) int pageNumber,
                                                                                  @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) int pageSize,
                                                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy) {
        return ResponseEntity.ok(this.userService.getAllUsersWithPaginationAndSorting(pageNumber, pageSize, sortBy));
    }
}
