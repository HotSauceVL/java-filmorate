package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ResourceBundle;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource getDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:file:./db/filmorate");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

  //  @Bean("resourceBundle")
    public ResourceBundle resourceBundle() {
        ResourceBundle rb = ResourceBundle.getBundle("query");
        return rb;
    }
}
