package co.com.pragma.powerup.r2dbc;

import co.com.pragma.powerup.model.user.gateways.TransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionAdapter implements TransactionGateway {

    private final TransactionalOperator txOperator;

    @Override
    public <T> Mono<T> doInTransaction(Mono<T> action) {
        return txOperator.transactional(action);
    }
}
