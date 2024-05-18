package test.prometheus.jdbc_repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import test.prometheus.aspects.TraceCallStack;
import test.prometheus.model.entity.Specialties;

import java.util.Optional;

@Repository
public interface SpecialtiesRepository extends CrudRepository<Specialties, Long> {

    @TraceCallStack
    Optional<Specialties> findById(Long id);
}
