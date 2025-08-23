package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
}
