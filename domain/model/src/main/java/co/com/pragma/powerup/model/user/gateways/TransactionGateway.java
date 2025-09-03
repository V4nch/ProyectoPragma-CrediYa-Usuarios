package co.com.pragma.powerup.model.user.gateways;

import reactor.core.publisher.Mono;


public interface TransactionGateway {
    <T> Mono<T> doInTransaction(Mono<T> action);
}
