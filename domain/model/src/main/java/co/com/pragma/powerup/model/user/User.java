package co.com.pragma.powerup.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long idUser;
    private String emailAddress;
    private String name;
    private String lastName;
    private Date birthDate;
    private String address;
    private String phoneNumber;
    private String baseSalary;
}


