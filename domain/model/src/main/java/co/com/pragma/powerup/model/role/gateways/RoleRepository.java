package co.com.pragma.powerup.model.role.gateways;


import co.com.pragma.powerup.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findByName(String name);

}
