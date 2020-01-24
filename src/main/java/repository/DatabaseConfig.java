package repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    private DataSource dataSource;
    private ResourceLoader resourceLoader;

    @Autowired
    public DatabaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public DataSource dataSource() {
        try {
            Resource resource = resourceLoader.getResource("classpath:sql/initialize.sql");
            String initializeSql = IOUtils.toString(resource.getInputStream());

            ComboPooledDataSource dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("org.h2.Driver");
            dataSource.setJdbcUrl(dataSourceUrl);
            dataSource.setUser(username);
            dataSource.setPassword(password);

            this.dataSource = dataSource;
            Connection connection = this.dataSource.getConnection();
            System.out.println("Connection established to: " +
                    "database: " + connection.getMetaData().getDatabaseProductName() +
                    " version: " + connection.getMetaData().getDatabaseProductVersion());

            connection.prepareStatement(initializeSql).execute();
        } catch (Exception e) {
            e.printStackTrace();
            dataSource = null;
        }
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
