package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.model.user.utils.Constants;
import co.com.pragma.powerup.r2dbc.entity.UserEntity;
import co.com.pragma.powerup.r2dbc.entity.UserWithRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MyReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmailAddress(String email);
    Mono<UserEntity> findByIdCard(String idCard);
    @Query(Constants.QUERY_USER_AUTH)
    Mono<UserWithRole> findUserWithRoleByEmail(@Param(Constants.EMAIL_ADDRESS) String email);

}
