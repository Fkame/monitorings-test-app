package test.prometheus.jdbc_repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.prometheus.aspects.trace_call_stack.TraceCallStack;
import test.prometheus.model.entity.Speciality;

import java.util.Optional;

/**
 * Репозиторий доступа к сущности спецциализаций в пет-клинике.
 */
@Repository
public interface SpecialtiesRepository extends CrudRepository<Speciality, Long> {

    /**
     * Найти специльность по идентификатору.
     * @param id иеднтификатор специальности в БД.
     * @return сущность специальность, если была найдена.
     */
    @TraceCallStack
    Optional<Speciality> findById(Long id);
}
