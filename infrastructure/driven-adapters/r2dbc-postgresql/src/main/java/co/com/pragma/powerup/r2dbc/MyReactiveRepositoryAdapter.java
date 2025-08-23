package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.model.user.gateways.UserRepository;
import co.com.pragma.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
    MyReactiveRepository
> implements UserRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));

    }

}
