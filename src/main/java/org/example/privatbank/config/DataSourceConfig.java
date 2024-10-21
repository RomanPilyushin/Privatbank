package org.example.privatbank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Configuration class for setting up the application's data source.
 * Attempts to connect to H2 database first; if it fails, switches to PostgreSQL.
 */
@Configuration
public class DataSourceConfig {

    // H2 Database properties

    /** The URL for the H2 database */
    @Value("${spring.datasource.url}")
    private String h2Url;

    /** The driver class name for the H2 database */
    @Value("${spring.datasource.driver-class-name}")
    private String h2DriverClassName;

    /** The username for the H2 database */
    @Value("${spring.datasource.username}")
    private String h2Username;

    /** The password for the H2 database */
    @Value("${spring.datasource.password}")
    private String h2Password;

    // PostgreSQL Database properties

    /** The URL for the PostgreSQL database */
    @Value("${backup.datasource.url}")
    private String postgresUrl;

    /** The driver class name for the PostgreSQL database */
    @Value("${backup.datasource.driver-class-name}")
    private String postgresDriverClassName;

    /** The username for the PostgreSQL database */
    @Value("${backup.datasource.username}")
    private String postgresUsername;

    /** The password for the PostgreSQL database */
    @Value("${backup.datasource.password}")
    private String postgresPassword;

    /**
     * Configures and returns the DataSource bean.
     * Tries to connect to the H2 database first; if it fails, switches to PostgreSQL.
     *
     * @return DataSource the configured data source
     */
    @Bean
    public DataSource dataSource() {
        try {
            // Create a new DataSource for H2
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            // Set the driver class name for H2
            dataSource.setDriverClassName(h2DriverClassName);
            // Set the URL for H2
            dataSource.setUrl(h2Url);
            // Set the username for H2
            dataSource.setUsername(h2Username);
            // Set the password for H2
            dataSource.setPassword(h2Password);
            // Try to establish a connection to H2
            dataSource.getConnection().close();
            // If successful, print a confirmation message
            System.out.println("Connected to H2 database.");
            // Return the configured H2 DataSource
            return dataSource;
        } catch (Exception e) {
            // If connection to H2 fails, print an error message
            System.err.println("Failed to connect to H2. Switching to PostgreSQL.");
            // Create a new DataSource for PostgreSQL
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            // Set the driver class name for PostgreSQL
            dataSource.setDriverClassName(postgresDriverClassName);
            // Set the URL for PostgreSQL
            dataSource.setUrl(postgresUrl);
            // Set the username for PostgreSQL
            dataSource.setUsername(postgresUsername);
            // Set the password for PostgreSQL
            dataSource.setPassword(postgresPassword);
            // Return the configured PostgreSQL DataSource
            return dataSource;
        }
    }
}
