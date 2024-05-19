package test.app.monitorings.aspects.trace_call_stack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Маркер для методов, трейс вызовов которых нужно отследить и замониторить.
 * <p>Если вешается на класс, то распространяется на все публичные методы класса.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceCallStack {

    /**
     * Какая-то кастомная метаинформация о вызове, можно там метку передать или что-то такое.
     * <p>Пока не используется.
     * @return метаинформация об отслеживаемом методе.
     */
    String metainfo() default "";
}
