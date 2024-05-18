package test.prometheus.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class TraceCallStackAspect {

    // test.prometheus.service.TestService$$SpringCGLIB$$0.doSomething(<generated>)

    private static final String CGLIB_PROXY_PART_PATTERN = "\\$\\$SpringCGLIB\\$\\$[0-9]+";

    private static final String ARGUMENTS_PART_PATTERN = "\\([\\s\\w]\\)";

    @Value("${aspects.trace-call-stack.package-search-prefixes}")
    private List<String> packagePrefixesToFind;

    @Value("${aspects.trace-call-stack.clean-cglib-prefixes-in-class-names}")
    private boolean needToCleanCglibPrefix;

    @Before("@annotation(test.prometheus.aspects.TraceCallStack)")
    public void traceTargetCall(JoinPoint joinPoint) {
        log.info("JoinPoint:\n{}", joinPoint);

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> filteredStackTrace = applyStackFiltration(List.of(stackTrace));

        log.info("Filtered stack trace:\n{}", filteredStackTrace.stream()
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"))
        );

        List<String> extractedStringFormats = extractStringFormatOfCallSignature(filteredStackTrace);

        Signature joinPointSignature = joinPoint.getSignature();
        String currentCallPoint = String.format("%s.%s",
                joinPointSignature.getDeclaringTypeName(), joinPointSignature.getName()
        );

        List<String> viewOfCallsTraceBeforeClear = new ArrayList<>(extractedStringFormats);
        viewOfCallsTraceBeforeClear.add(0, currentCallPoint);

        List<String> finalViewOfCallsTrace = cleanIfNeed(viewOfCallsTraceBeforeClear);

        List<String> deduplicatedCallsTrace = new LinkedHashSet<>(finalViewOfCallsTrace).stream()
                .sorted(Collections.reverseOrder())
                .toList();

        log.info("End of aspect logic, final view:\n{}", String.join("\n", deduplicatedCallsTrace));

        log.info("Here your trace till annotation call:\n{}", String.join(" -> ", deduplicatedCallsTrace));
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
}
