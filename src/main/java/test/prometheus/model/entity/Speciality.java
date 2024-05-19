package test.prometheus.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Сущность должности в пет-клинике.
 */
@Data
public class Speciality {
    @Id
    private Long id;
    private String name;
}
