package test.prometheus.config.datasource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    @Bean("postgresDataSource")
    public DataSource createPostgresDataSource(PostgresDataSourceProperties props) {
        log.debug("\n" +
                        "----------------- Postgres Datasource Properties -----------------\n" +
                        "\turl = {}\n" +
                        "\tusername = {}\n" +
                        "\tpassword = {}\n" +
                        "\tdriverClassName = {}\n" +
                        "----------------- Postgres Datasource Properties -----------------",
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
