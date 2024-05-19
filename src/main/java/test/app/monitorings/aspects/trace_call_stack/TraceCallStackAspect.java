package test.app.monitorings.aspects.trace_call_stack;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Аспект для трейсирования и мониторинга вызовов отслеживаемых методов.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TraceCallStackAspect {
    private final TracingStackTraceService tracingStackTraceService;

    /**
     * Отследить вызов и время выполнения маркированного метода.
     * @param joinPoint точка внедрения, по факту эта точка - вызов того или иного помеченного метода,
     *                  который перехватывается данным аспектом.
     * @return результат работы метода.
     * @throws Throwable если метод выбросит ошибку - она также пробросится.
     */
    @Around("@annotation(test.app.monitorings.aspects.trace_call_stack.TraceCallStack)")
    public Object traceTargetCall(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("Aspect reacted on: {}, kind = {}, args = {}",
                joinPoint.getSignature(), joinPoint.getKind(), Arrays.toString(joinPoint.getArgs())
        );

        List<StackTraceElement> stackTrace = List.of(Thread.currentThread().getStackTrace());

        TracingStackTraceServiceInput.TracingStackTraceServiceInputBuilder traceServiceInputBuilder =
                TracingStackTraceServiceInput.builder()
                        .stackTrace(stackTrace)
                        .calledMethodSignature(joinPoint.getSignature());

        StopWatch executionTime = new StopWatch();

        Optional<Throwable> exceptionWasThrown = Optional.empty();
        Object result = null;
        try {
            executionTime.start();
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable ex) {
            exceptionWasThrown = Optional.of(ex);
            // пока что подавим, чтобы выкинуть потом
        } finally {
            executionTime.stop();

            traceServiceInputBuilder
                    .executionException(exceptionWasThrown)
                    .executionTimeInMills(executionTime.getTotalTimeMillis());
        }

        proceedMonitoringsAsync(traceServiceInputBuilder);

        if (exceptionWasThrown.isPresent()) {
            throw exceptionWasThrown.get();
        }

        return result;
    }

    private void proceedMonitoringsAsync(TracingStackTraceServiceInput.TracingStackTraceServiceInputBuilder builder) {
        CompletableFuture.runAsync(() -> tracingStackTraceService
                .doTraceLogic(builder.build())
        ).exceptionally(throwable -> {
            log.error("Ошибка при отправке данных мониторинга трейса: ", throwable);

            // TODO:: Отправлять в Sentry
            return null;
        });
    }

}
