package test.prometheus.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "datasource.postgres")
public class PostgresDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String jdbcDriver;
}
