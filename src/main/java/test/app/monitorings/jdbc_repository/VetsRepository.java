package test.app.monitorings.jdbc_repository;

import org.springframework.data.repository.CrudRepository;
import test.app.monitorings.model.entity.Vet;
import test.app.monitorings.aspects.trace_call_stack.TraceCallStack;

/**
 * Репозиторий доступа к сущности ветеринара в пет-клинике.
 */
@TraceCallStack
public interface VetsRepository extends CrudRepository<Vet, Long> {
}
