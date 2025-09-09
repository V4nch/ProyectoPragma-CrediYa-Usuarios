package co.com.pragma.powerup.r2dbc;


import co.com.pragma.powerup.model.role.Role;
import co.com.pragma.powerup.r2dbc.entity.RoleEntity;
import co.com.pragma.powerup.model.role.gateways.RoleRepository;
import co.com.pragma.powerup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository
        > implements RoleRepository {

    private final RoleReactiveRepository myRepository;

    public RoleRepositoryAdapter(RoleReactiveRepository repository,
                                       ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.myRepository = repository;

    }

    @Override
    public Mono<Role> findByName(String name){
        return this.myRepository.findByName(name).map(d -> mapper.map(d, Role.class));
    }


}
