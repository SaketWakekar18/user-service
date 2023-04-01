package app.userservice.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String userId;
    @NotEmpty
    @Size(min = 5, message = "Name must be of atleast 5 characters")
    private String name;
    @Email(message = "Email address not valid")
    private String email;
    @NotEmpty(message = "About cannot be empty")
    private String about;
    @Transient
    private List<Rating> ratings = new ArrayList<>();
}
