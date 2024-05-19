package test.prometheus.rest.actuator_ext;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Кастомный тестовый эндпоинт для актуатора,
 * реализованный с помощью Endpoint возможностей без использования Spring MVC.
 * <p>Эндпоинт генерирует случайные UID.
 */
@Component
@WebEndpoint(id = "uid")
public class UidGenerator {

    /**
     * Сгенерировать UID.
     * @param amount количество для генерации.
     * @return сгенерированные uids.
     */
    @ReadOperation
    public List<UUID> generate(@Nullable Integer amount) {
        if (amount == null) {
            amount = 1;
        }

        List<UUID> uuids = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            uuids.add(UUID.randomUUID());
        }
        return uuids;
    }

    /**
     * Сгенерировать указанное количество чисел в указанном интервале.
     * <p>amount передается в url: uid/{amount}, before и after - поля в json.
     *
     * @param amount количество элементов для генерации.
     * @param before начало интервала для генерации.
     * @param after окончание интервала для генерации.
     *
     * @return сгенерированные uids.
     */
    @WriteOperation
    public List<String> generateMultiple(@Selector(match = Selector.Match.SINGLE) int amount,
                                        String before, String after) {
        List<String> uuids = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            uuids.add(before + UUID.randomUUID() + after);
        }
        return uuids;
    }
}
