package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.model.user.User;
import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.r2dbc.entity.UserEntity;
import co.com.pragma.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Long,
        MyReactiveRepository
> implements UserRepository {

    private final MyReactiveRepository myRepository;

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository,
                                       ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.myRepository = repository;

    }

    @Override
    public Mono<User> findByEmail(String email){
        return this.myRepository.findByEmailAddress(email).map(d -> mapper.map(d, User.class));
    }
}

