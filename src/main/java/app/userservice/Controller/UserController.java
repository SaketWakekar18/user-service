package app.userservice.Controller;

import app.userservice.Entity.User;
import app.userservice.Payloads.APIResponse;
import app.userservice.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userId) {
        User updatedUser = this.userService.updateUser(user, userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = this.userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/getUserByID/{userId}")
    public ResponseEntity<User> getUserByID(@PathVariable String userId) {
        User userByID = this.userService.getUserByID(userId);
        return ResponseEntity.ok(userByID);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable String userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity(new APIResponse("User deleted Successfully!!", true, HttpStatus.GONE), HttpStatus.OK);
    }
}
