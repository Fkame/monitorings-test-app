package test.app.monitorings.integration;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.app.monitorings.annotations.IntegrationLocalTest;
import test.app.monitorings.utils.DtoValidator;

import java.util.List;

@IntegrationLocalTest
public class DtoValidatorTest {

    @Autowired
    private DtoValidator testTarget;

    @Test
    public void testThatValidateWithoutNullToleranceWorksFine() {
        Assertions.assertFalse(testTarget.validateWithoutNullTolerance(null)
                .isEmpty()
        );

        List<String> errors;

        errors = testTarget.validateWithoutNullTolerance(TestDto.builder()
                .str("")
                .build()
        );
        Assertions.assertEquals(1, errors.size());

        errors = testTarget.validateWithoutNullTolerance(TestDto.builder()
                .str("1235324324")
                .build()
        );
        Assertions.assertTrue(errors.isEmpty());
    }

    @Data
    @Builder
    static class TestDto {

        @NotBlank
        private String str;
    }
}
