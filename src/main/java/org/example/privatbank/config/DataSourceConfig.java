package org.example.privatbank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String h2Url;

    @Value("${spring.datasource.driver-class-name}")
    private String h2DriverClassName;

    @Value("${spring.datasource.username}")
    private String h2Username;

    @Value("${spring.datasource.password}")
    private String h2Password;

    @Value("${backup.datasource.url}")
    private String postgresUrl;

    @Value("${backup.datasource.driver-class-name}")
    private String postgresDriverClassName;

    @Value("${backup.datasource.username}")
    private String postgresUsername;

    @Value("${backup.datasource.password}")
    private String postgresPassword;

    @Bean
    public DataSource dataSource() {
        try {
            // Try to connect to H2
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(h2DriverClassName);
            dataSource.setUrl(h2Url);
            dataSource.setUsername(h2Username);
            dataSource.setPassword(h2Password);
            // Try a connection
            dataSource.getConnection().close();
            System.out.println("Connected to H2 database.");
            return dataSource;
        } catch (Exception e) {
            // If H2 fails, connect to PostgreSQL
            System.err.println("Failed to connect to H2. Switching to PostgreSQL.");
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(postgresDriverClassName);
            dataSource.setUrl(postgresUrl);
            dataSource.setUsername(postgresUsername);
            dataSource.setPassword(postgresPassword);
            return dataSource;
        }
    }
}
