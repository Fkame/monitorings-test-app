package test.prometheus.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Сущность ветеринара-сотрудника.
 */
@Data
public class Vet {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
}
