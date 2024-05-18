package test.prometheus.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.prometheus.model.entity.Specialties;
import test.prometheus.jdbc_repository.SpecialtiesRepository;
import test.prometheus.service.TestService;

@RestController
@RequestMapping("/specialities")
@RequiredArgsConstructor
public class SpecialtesController {

    private final SpecialtiesRepository specialtiesRepository;
    private final TestService testService;

    @GetMapping(value = "/{id}")
    public Specialties getById(@PathVariable(required = true, value = "id") Long id) {
        testService.doSomething();

        return specialtiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No specialities found!"));
    }
}
