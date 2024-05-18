package test.prometheus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.prometheus.aspects.TraceCallStack;

@Service
@Slf4j
public class TestService {

    @TraceCallStack
    public void doSomething() {
        log.info("i do!!!");
    }
}
