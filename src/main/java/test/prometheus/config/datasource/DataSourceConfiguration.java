package test.prometheus.config.datasource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Конфиг создания датасурса.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    /**
     * Создать датасурс постгреса.
     * @param props проперти постгреса из файла конфигурации.
     * @return подготовленный датасурс.
     */
    @Bean("postgresDataSource")
    public DataSource createPostgresDataSource(PostgresDataSourcePropertiesProvider props) {
        log.debug("""

                        ----------------- Postgres Datasource Properties -----------------
                        \turl = {}
                        \tusername = {}
                        \tpassword = {}
                        \tdriverClassName = {}
                        ----------------- Postgres Datasource Properties -----------------""",
                props.getUrl(), props.getUsername(), props.getPassword(), props.getJdbcDriver()
        );

        return DataSourceBuilder.create()
                .driverClassName(props.getJdbcDriver())
                .url(props.getUrl())
                .username(props.getUsername())
                .password(props.getPassword())
                .build();
    }
}
