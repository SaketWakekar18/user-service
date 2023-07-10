package app.userservice;

import app.userservice.Entity.Rating;
import app.userservice.External.Services.RatingService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
	@Autowired
    private RatingService ratingService;

    @Test
    void contextLoads() {
    }

	@Test
    void createRating() {
		Rating rating = Rating.builder()
				.rating(10)
				.userId(6)
				.hotelId(1)
				.feedback("This is created using feign client")
				.build();
		Rating serviceRating = ratingService.createRating(rating);
		System.out.println("Service Rating: "+ serviceRating);
	}

}
