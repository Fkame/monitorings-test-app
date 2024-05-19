package test.prometheus.integration;
;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import test.prometheus.annotations.IntegrationLocalTest;
import test.prometheus.aspects.trace_call_stack.TraceCallStackAspect;
import test.prometheus.aspects.trace_call_stack.TracingStackTraceService;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationLocalTest
public class TraceCallStackAspectTest {

    @Autowired
    private TraceCallStackAspect testTarget;

    @MockBean
    private TracingStackTraceService tracingStackTraceService;

    @Test
    @SneakyThrows
    public void testThatAsyncCallReallyWorks() {
        int serviceWorkTime = 500;
        int awaitToCheck = serviceWorkTime + 100;
        AtomicBoolean isWorking = new AtomicBoolean(true);

        // Создаём логику, которая осуществляет имитацию бурной работы сервиса
        doAnswer(invocation -> {
            try {
                Thread.sleep(serviceWorkTime);
                isWorking.set(false);
                return null;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).when(tracingStackTraceService).doTraceLogic(any());

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed(any()))
                .thenReturn(new Object());
        when(joinPoint.getSignature())
                .thenReturn(new TestSignature());

        testTarget.traceTargetCall(joinPoint);
        Assertions.assertTrue(isWorking.get());

        Thread.sleep(awaitToCheck);
        Assertions.assertFalse(isWorking.get());
    }

    static class TestSignature implements Signature {

        @Override
        public String toShortString() {
            return null;
        }

        @Override
        public String toLongString() {
            return null;
        }

        @Override
        public String getName() {
            return TestSignature.class.getSimpleName();
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public Class getDeclaringType() {
            return TestSignature.class;
        }

        @Override
        public String getDeclaringTypeName() {
            return TestSignature.class.getPackageName();
        }
    }
}
