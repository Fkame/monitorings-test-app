package test.prometheus.rest.actuator_ext;

import lombok.Data;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Кастомный тестовый эндпоинт для актуатора, реализованный с помощью Spring MVC возможностей.
 * <p>Эндпоинт генерирует случайные числа.
 */
//@RestController
@RestControllerEndpoint(id = "number")
@Component
public class NumbersGenerator {

    private final Random rand = new Random();

    /**
     * Сгенерировать случайное число.
     * @param amount количество чисел для генерации.
     * @return случайные числа.
     */
    @GetMapping
    public List<Long> generate(@RequestParam(required = false, defaultValue = "1") int amount) {
        List<Long> numbers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            numbers.add(rand.nextLong());
        }

        return numbers;
    }

    /**
     * Сгенерировать случайные числа в указанном диапазоне.
     * @param amount количество чисел для генерации.
     * @param range интервал для генерации.
     * @return сгенерированные числа.
     */
    @PostMapping
    public List<Long> generate(@RequestParam(required = false, defaultValue = "1") int amount,
                               @RequestBody Range range) {
        List<Long> numbers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            numbers.add(rand.nextLong(range.getStart(), range.getEnd()));
        }

        return numbers;
    }

    /**
     * Интервал для генерации чисел.
     */
    @Data
    static class Range {
        private long start = 1;
        private long end = 10;
    }
}
