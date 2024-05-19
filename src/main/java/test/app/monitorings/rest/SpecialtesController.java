package test.app.monitorings.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.app.monitorings.jdbc_repository.SpecialtiesRepository;
import test.app.monitorings.model.entity.Speciality;

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
