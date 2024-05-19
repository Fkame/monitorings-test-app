package test.prometheus.aspects.trace_call_stack;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.Signature;

import java.util.List;
import java.util.Optional;

/**
 * Входные данные для сервиса {@link TracingStackTraceService}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TracingStackTraceServiceInput {

    @NotNull
    private List<StackTraceElement> stackTrace;

    @Builder.Default
    private Optional<Throwable> executionException = Optional.empty();

    @NotNull
    private Signature calledMethodSignature;

    @NotNull
    private Long executionTimeInMills;
}
