package co.com.pragma.powerup.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    private Long idUser;   // Primary Key
    private String emailAddress;
    private String name;
    private String lastName;
    private Date birthDate;
    private String address;
    private String phoneNumber;
    private String salaryBase;



}
