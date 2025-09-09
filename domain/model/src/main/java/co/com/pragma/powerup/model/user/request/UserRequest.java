package co.com.pragma.powerup.model.user.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserRequest {

    private String idCard;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String emailAddress;
    private String baseSalary;
    private String password;
    private String roleName;
}
