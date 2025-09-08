package co.com.pragma.powerup.model.user.gateways;

import co.com.pragma.powerup.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserRepository{

         Mono<User> save(User user);
         Mono<User> findByEmail(String email);
         Mono<User> findByIdCard(String idCard);



}
