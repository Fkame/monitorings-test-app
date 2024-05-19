package test.app.monitorings.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DtoValidator {

    public static final String FIELD_AND_MESSAGE_FORMAT = "[%s] - %s";

    private final Validator validator;

    public <T> List<String> validate(T dto) {
        if (dto == null) {
            return Collections.emptyList();
        }

        return applyValidationAndParseErrors(dto);
    }

    public <T> List<String> validateWithoutNullTolerance(T dto) {
        if (dto == null) {
            return Collections.singletonList("Объект пустой");
        }

        return applyValidationAndParseErrors(dto);
    }

    private <T> List<String> applyValidationAndParseErrors(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        List<String> errors = new ArrayList<>(violations.size());

        for (ConstraintViolation<T> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorText = violation.getMessage();

            errors.add(String.format(FIELD_AND_MESSAGE_FORMAT, fieldName, errorText));
        }

        return errors;
    }
}
