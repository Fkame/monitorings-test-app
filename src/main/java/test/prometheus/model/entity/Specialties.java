package test.prometheus.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Specialties {
    @Id
    private Long id;
    private String name;
}
