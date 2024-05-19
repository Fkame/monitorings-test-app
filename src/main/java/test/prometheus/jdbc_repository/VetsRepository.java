package test.prometheus.jdbc_repository;

import org.springframework.data.repository.CrudRepository;
import test.prometheus.aspects.trace_call_stack.TraceCallStack;
import test.prometheus.model.entity.Vet;

/**
 * Репозиторий доступа к сущности ветеринара в пет-клинике.
 */
@TraceCallStack
public interface VetsRepository extends CrudRepository<Vet, Long> {
}
