package test.prometheus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.prometheus.model.entity.Speciality;
import test.prometheus.jdbc_repository.SpecialtiesRepository;

/**
 * Контроллер работы с специальностями в пет-клинике.
 */
@RestController
@RequestMapping("/specialities")
@RequiredArgsConstructor
public class SpecialtesController {

    private final SpecialtiesRepository specialtiesRepository;

    /**
     * Получить информацию о специальности по идентификатору.
     * @param id идентификатор специальности.
     * @return сущность специальности из БД.
     */
    @GetMapping(value = "/{id}")
    public Speciality getById(@PathVariable(value = "id") Long id) {
        return specialtiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No specialities found!"));
    }
}
