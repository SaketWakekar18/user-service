package app.userservice.Services.Implementation;

import app.userservice.Entity.Hotels;
import app.userservice.Entity.Rating;
import app.userservice.Entity.User;
import app.userservice.Exceptions.EmailAlreadyExistsException;
import app.userservice.Exceptions.ResourceNotFoundException;
import app.userservice.External.Services.HotelService;
import app.userservice.External.Services.RatingService;
import app.userservice.Payloads.UserResponse;
import app.userservice.Repository.UserRepository;
import app.userservice.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final HotelService hotelService;
    private final RatingService ratingService;

    @Override
    public User createUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            User savedUser = this.userRepository.save(user);
            return savedUser;
        } else {
            throw new EmailAlreadyExistsException("Email already exists!");
        }
    }

    @Override
    public User updateUser(User user, int userId) {
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
    public User getUserByID(int userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server " + userId));
        Rating[] userRatings = restTemplate.getForObject("http://rating-service/ratings/ratingsByUser/" + user.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.stream(userRatings).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
//            ResponseEntity<Hotels> hotelsList = restTemplate.getForEntity("http://hotel-service/hotels/getHotelsByID/" + rating.getHotelId(), Hotels.class);
//            Hotels body = hotelsList.getBody();
            Hotels body = this.hotelService.getHotels(rating.getHotelId());
            rating.setHotels(body);
            return rating;
        }).collect(Collectors.toList());
        user.getRatings().addAll(ratingList);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        User deletedUser = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given ID not found on server " + userId));
        this.userRepository.delete(deletedUser);
    }

    @Override
    public User searchUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
//        ArrayList<Rating> userRatings = restTemplate.getForObject("http://localhost:8082/ratings/ratingsByUser/" + user.getUserId(), ArrayList.class);
//        user.setRatings(userRatings);
        return user;
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
