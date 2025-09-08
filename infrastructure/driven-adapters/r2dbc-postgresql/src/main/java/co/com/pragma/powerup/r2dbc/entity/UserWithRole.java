package co.com.pragma.powerup.r2dbc.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWithRole {
    private String emailAddress;
    private String password;
    private String roleName;
}
