package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.r2dbc.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmailAddress(String email);
    Mono<UserEntity> findByIdCard(String idCard);

}
