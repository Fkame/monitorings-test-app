package test.prometheus.rest.actuator_ext;

import lombok.Data;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@RestController
@RestControllerEndpoint(id = "number")
@Component
public class NumbersGenerator {

    private final Random rand = new Random();

    @GetMapping
    public List<Long> generate(@RequestParam(required = false, defaultValue = "1") int amount) {
        List<Long> numbers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            numbers.add(rand.nextLong());
        }

        return numbers;
    }

    @PostMapping
    public List<Long> generate(@RequestParam(required = false, defaultValue = "1") int amount,
                               @RequestBody Range range) {
        List<Long> numbers = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            numbers.add(rand.nextLong(range.getStart(), range.getEnd()));
        }

        return numbers;
    }

    @Data
    static class Range {
        private long start = 1;
        private long end = 10;
    }
}
