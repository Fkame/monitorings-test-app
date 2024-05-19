package test.prometheus.aspects.trace_call_stack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import test.prometheus.utils.DtoValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * Сервис для подготовки и отправки в систему мониторинга полученной информации о выполнении наблюдаемого метода.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TracingStackTraceService {

    private static final String CGLIB_PROXY_PART_PATTERN = "\\$\\$SpringCGLIB\\$\\$[0-9]+";
    private static final String ARGUMENTS_PART_PATTERN = "\\([\\s\\w]\\)";

    @Value("${aspects.trace-call-stack.package-search-prefixes}")
    private List<String> packagePrefixesToFind;

    @Value("${aspects.trace-call-stack.clean-cglib-prefixes-in-class-names:false}")
    private boolean needToCleanCglibPrefix;

    private final DtoValidator validator;

    /**
     * Выполнить логику пост-обработки собранных данных о выполнении/вызове отслеживаемого метода
     * и отправить данные в систему мониторинга.
     * @param input данные для пост-обработки и отправке в систему мониторинга.
     */
    public void doTraceLogic(@NonNull TracingStackTraceServiceInput input) {
        List<String> errors = validator.validateWithoutNullTolerance(input);
        if (!errors.isEmpty()) {
            throw new RuntimeException(String.format(
                    "Ошибка валидации входной dto: %s", String.join("; ", errors)
            ));
        }

        log.info("Data for Monitoring: {}", input);

        // Убираем из стек-трейса не интересующие нас уровни. Это при необходимости.
        List<StackTraceElement> filteredStackTrace = applyStackFiltration(input.getStackTrace());

        /*
        log.info("Filtered stack trace:\n{}", filteredStackTrace.stream()
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"))
        );
         */

        // Форматируем элементы стектрейса в шаблон: полное_имя_класса.имя_метода
        List<String> extractedStringFormats = extractStringFormatOfCallSignature(filteredStackTrace);

        Signature joinPointSignature = input.getCalledMethodSignature();
        String currentCallPoint = String.format("%s.%s",
                joinPointSignature.getDeclaringTypeName(), joinPointSignature.getName()
        );

        // Сливаем стектрейс вызовов и вызванный метод, на котором сработал перехватчик.
        List<String> viewOfCallsTraceBeforeClear = new ArrayList<>(extractedStringFormats);
        viewOfCallsTraceBeforeClear.add(0, currentCallPoint);

        // Вычищаем из имен классов суффиксы проксей. Это при необходимости.
        List<String> finalViewOfCallsTrace = cleanIfNeed(viewOfCallsTraceBeforeClear);

        // Чистим дубликаты элементов стектрейса. Могли появиться после всех манипуляций выше.
        List<String> deduplicatedCallsTrace = new LinkedHashSet<>(finalViewOfCallsTrace).stream()
                .sorted(Collections.reverseOrder())
                .toList();

        /*
        log.info("End of aspect logic, final view:\n{}", String.join("\n", deduplicatedCallsTrace));
         */

        log.info("Here your trace till annotation call:\n{}", String.join(" -> ", deduplicatedCallsTrace));
    }

    private List<StackTraceElement> applyStackFiltration(List<StackTraceElement> callStackTrace) {
        List<StackTraceElement> elements = callStackTrace.stream()
                .filter(Objects::nonNull).toList();

        List<StackTraceElement> filtered = new ArrayList<>();
        for (StackTraceElement item : elements) {
            boolean isStartsWithOneOfRequiredPackagePrefixes = packagePrefixesToFind.stream()
                    .anyMatch(packagePrefix -> item.getClassName().startsWith(packagePrefix));

            if (isStartsWithOneOfRequiredPackagePrefixes) {
                filtered.add(item);
            }
        }

        // Необходимо убрать из stacktrace вызов метода-эдвайса данного аспекта
        filtered.removeIf(item -> Objects.equals(item.getClassName(), this.getClass().getName()));

        return filtered;
    }

    private List<String> cleanIfNeed(List<String> stringFormatsCallSignatures) {
        if (!needToCleanCglibPrefix) {
            return stringFormatsCallSignatures;
        }

        return stringFormatsCallSignatures.stream()
                .map(item -> item.replaceAll(CGLIB_PROXY_PART_PATTERN, ""))
                .toList();
    }

    private List<String> extractStringFormatOfCallSignature(List<StackTraceElement> filteredStackTrace) {
        return filteredStackTrace.stream()
                .map(item -> String.format("%s.%s", item.getClassName(), item.getMethodName()))
                .toList();
    }
}
