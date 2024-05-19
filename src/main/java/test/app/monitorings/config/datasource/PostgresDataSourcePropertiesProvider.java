package test.app.monitorings.config.datasource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Проперти датасурса постгреса из файла конфигурации.
 */
@Data
@Component
@ConfigurationProperties(prefix = "datasource.postgres")
@Validated
public class PostgresDataSourcePropertiesProvider {

    private static final String ERROR_MSG_TEMPLATE =
            "PostgresDataSourcePropertiesProvider.%s поле уже было инициализировано, " +
                    "обнаружена попытка перезаписи значения!";

    @NotBlank
    private String url;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String jdbcDriver;

    /**
     * Записать поле url. Если уже было инициализировано, повторное изменение вызовет ошибку.
     * @param url url базы постгреса.
     */
    public void setUrl(String url) {
        if (StringUtils.isNotBlank(this.url)) {
            throw new RuntimeException(String.format(ERROR_MSG_TEMPLATE, "url"));
        }

        this.url = url;
    }

    /**
     * Записать поле username. Если уже было инициализировано, повторное изменение вызовет ошибку.
     * @param username username пользователя базы постгреса.
     */
    public void setUsername(String username) {
        if (StringUtils.isNotBlank(this.username)) {
            throw new RuntimeException(String.format(ERROR_MSG_TEMPLATE, "username"));
        }

        this.username = username;
    }

    /**
     * Записать поле password. Если уже было инициализировано, повторное изменение вызовет ошибку.
     * @param password password пользователя базы постгреса.
     */
    public void setPassword(String password) {
        if (StringUtils.isNotBlank(this.password)) {
            throw new RuntimeException(String.format(ERROR_MSG_TEMPLATE, "password"));
        }

        this.password = password;
    }

    /**
     * Записать поле jdbcDriver. Если уже было инициализировано, повторное изменение вызовет ошибку.
     * @param jdbcDriver тип jdbc драйвера базы постгреса.
     */
    public void setJdbcDriver(String jdbcDriver) {
        if (StringUtils.isNotBlank(this.jdbcDriver)) {
            throw new RuntimeException(String.format(ERROR_MSG_TEMPLATE, "jdbcDriver"));
        }

        this.jdbcDriver = jdbcDriver;
    }
}
